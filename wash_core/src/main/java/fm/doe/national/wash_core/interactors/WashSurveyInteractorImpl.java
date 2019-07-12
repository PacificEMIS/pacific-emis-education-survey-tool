package fm.doe.national.wash_core.interactors;

import android.content.Context;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import fm.doe.national.core.data.model.Progressable;
import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.data.model.mutable.MutableProgress;
import fm.doe.national.wash_core.data.data_source.WashDataSource;
import fm.doe.national.wash_core.data.model.Answer;
import fm.doe.national.wash_core.data.model.WashSurvey;
import fm.doe.national.wash_core.data.model.mutable.MutableAnswer;
import fm.doe.national.wash_core.data.model.mutable.MutableGroup;
import fm.doe.national.wash_core.data.model.mutable.MutableQuestion;
import fm.doe.national.wash_core.data.model.mutable.MutableSubGroup;
import fm.doe.national.wash_core.data.model.mutable.MutableWashSurvey;
import fm.doe.national.wash_core.data.serialization.model.Relation;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;

public class WashSurveyInteractorImpl implements WashSurveyInteractor {

    private final WashDataSource washDataSource;
    private final PublishSubject<Survey> surveyPublishSubject = PublishSubject.create();
    private final PublishSubject<MutableGroup> groupPublishSubject = PublishSubject.create();
    private final PublishSubject<MutableSubGroup> subGroupPublishSubject = PublishSubject.create();
    private final WeakReference<Context> appContextRef;

    private MutableWashSurvey survey;
    private long currentGroupId;
    private long currentSubGroupId;
    private long currentQuestionId;

    public WashSurveyInteractorImpl(WashDataSource washDataSource, Context context) {
        this.washDataSource = washDataSource;
        this.appContextRef = new WeakReference<>(context);
    }

    @Override
    public Single<List<Survey>> getAllSurveys() {
        return washDataSource.loadAllSurveys()
                .flatMapObservable(Observable::fromIterable)
                .map(survey -> {
                    MutableWashSurvey mutableSurvey = new MutableWashSurvey((WashSurvey) survey);
                    initProgress(mutableSurvey);
                    return mutableSurvey;
                })
                .toList()
                .map(list -> new ArrayList<>(list));
    }

    @Override
    public void setCurrentSurvey(Survey survey) {
        setCurrentSurvey(survey, false);
    }

    @Override
    public void setCurrentSurvey(Survey survey, boolean shouldFetchProgress) {
        this.survey = (MutableWashSurvey) survey;
        if (shouldFetchProgress) {
            initProgress(this.survey);
        }
    }

    @Override
    public Survey getCurrentSurvey() {
        return survey;
    }

    private void initProgress(MutableWashSurvey survey) {
        if (survey.getGroups() != null) {
            survey.setProgress(reduceProgress(survey.getGroups().stream().peek(this::initProgress)));
        }
    }

    private void initProgress(MutableGroup group) {
        if (group.getSubGroups() != null) {
            group.setProgress(reduceProgress(group.getSubGroups().parallelStream().peek(this::initProgress)));
        }
    }

    private void initProgress(MutableSubGroup subGroup) {
        if (subGroup.getQuestions() != null) {
            int completed = (int) subGroup.getQuestions().parallelStream()
                    .filter(MutableQuestion::isAnswered)
                    .count();
            int total = (int) subGroup.getQuestions().parallelStream()
                    .filter(question -> isQuestionAvailable(subGroup, question))
                    .count();
            subGroup.setProgress(new MutableProgress(total, completed));
        }
    }

    @Override
    public Single<List<MutableGroup>> requestGroups() {
        return Single.fromCallable(() -> survey.getGroups());
    }

    @Override
    public Single<List<MutableSubGroup>> requestSubGroups(long groupId) {
        return requestGroups()
                .flatMapObservable(Observable::fromIterable)
                .filter(it -> it.getId() == groupId)
                .firstOrError()
                .map(MutableGroup::getSubGroups);
    }

    @Override
    public Single<List<MutableQuestion>> requestQuestions(long groupId, long subGroupId) {
        return requestSubGroups(groupId)
                .flatMapObservable(Observable::fromIterable)
                .filter(it -> it.getId() == subGroupId)
                .firstOrError()
                .map(MutableSubGroup::getQuestions);
    }

    @Override
    public Completable updateAnswer(Answer answer, long groupId, long subGroupId, long questionId) {
        return washDataSource.updateAnswer(answer)
                .flatMapCompletable(updatedAnswer -> Completable.fromAction(() -> notifyProgressChanged(
                        new MutableAnswer(updatedAnswer),
                        groupId,
                        subGroupId,
                        questionId
                )));
    }

    private void notifyProgressChanged(MutableAnswer answer,
                                       long groupId,
                                       long subGroupId,
                                       long questionId) {
        if (survey.getGroups() == null) {
            return;
        }

        MutableProgress updatedProgress = reduceProgress(
                survey.getGroups().parallelStream()
                        .peek(group -> {
                            if (group.getId() == groupId) {
                                updateProgressAndNotify(answer, group, subGroupId, questionId);
                            }
                        })
        );
        survey.setProgress(updatedProgress);

        if (!survey.isCompleted() && survey.getProgress().isFinished()) {
            survey.setCompleteDate(new Date());
            washDataSource.updateSurvey(survey);
        }

        surveyPublishSubject.onNext(survey);
    }

    private void updateProgressAndNotify(MutableAnswer answer,
                                         MutableGroup group,
                                         long subGroupId,
                                         long questionId) {
        if (group.getSubGroups() == null) {
            return;
        }

        MutableProgress updatedProgress = reduceProgress(
                group.getSubGroups().parallelStream()
                        .peek(subGroup -> {
                            if (subGroup.getId() == subGroupId) {
                                updateProgressAndNotify(answer, subGroup, questionId);
                            }
                        })
        );
        group.setProgress(updatedProgress);
        groupPublishSubject.onNext(group);
    }

    private void updateProgressAndNotify(MutableAnswer answer, MutableSubGroup subGroup, long questionId) {
        if (subGroup.getQuestions() == null) {
            return;
        }

        subGroup.getQuestions().stream().filter(q -> q.getId() == questionId).peek(q -> q.setAnswer(answer)).close();
        initProgress(subGroup);
        subGroupPublishSubject.onNext(subGroup);
    }

    private boolean isQuestionAvailable(MutableSubGroup subGroup, MutableQuestion child) {
        Optional<MutableQuestion> parentQuestionOp = findParentQuestion(subGroup, child);

        if (!parentQuestionOp.isPresent()) {
            return true;
        }

        Context context = appContextRef.get();

        if (context == null) {
            return false;
        }

        MutableQuestion parentQuestion = parentQuestionOp.get();
        Relation relation = child.getRelation();
        assert relation != null;

        return parentQuestion.isAnswerInRelation(context, relation);
    }

    private Optional<MutableQuestion> findParentQuestion(MutableSubGroup subGroup, MutableQuestion child) {
        if (child.getRelation() == null) {
            return Optional.empty();
        }

        String parentId = child.getRelation().getQuestionId();
        return subGroup.getQuestions().parallelStream()
                .filter(question -> question.getPrefix().equals(parentId))
                .findFirst();
    }

    @Override
    public Observable<Survey> getSurveyProgressObservable() {
        return surveyPublishSubject;
    }

    @Override
    public Observable<MutableGroup> getGroupProgressObservable() {
        return groupPublishSubject;
    }

    @Override
    public Observable<MutableSubGroup> getSubGroupProgressObservable() {
        return subGroupPublishSubject;
    }

    @Override
    public Completable updateAnswer(Answer answer) {
        return updateAnswer(answer, currentGroupId, currentSubGroupId, currentQuestionId);
    }

    @Override
    public long getCurrentGroupId() {
        return currentGroupId;
    }

    @Override
    public long getCurrentSubGroupId() {
        return currentSubGroupId;
    }

    @Override
    public long getCurrentQuestionId() {
        return currentQuestionId;
    }

    @Override
    public void setCurrentGroupId(long id) {
        currentGroupId = id;
    }

    @Override
    public void setCurrentSubGroupId(long id) {
        currentSubGroupId = id;
    }

    @Override
    public void setCurrentQuestionId(long id) {
        currentQuestionId = id;
    }

    private MutableProgress reduceProgress(@NonNull Stream<? extends Progressable> stream) {
        return (MutableProgress) stream
                .map(Progressable::getProgress)
                .reduce(MutableProgress.createEmptyProgress(), MutableProgress::plus);
    }
}
