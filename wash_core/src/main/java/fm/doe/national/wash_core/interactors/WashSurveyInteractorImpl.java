package fm.doe.national.wash_core.interactors;

import java.util.ArrayList;
import java.util.List;

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
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;

public class WashSurveyInteractorImpl implements WashSurveyInteractor {

    private final WashDataSource washDataSource;
    private final PublishSubject<Survey> surveyPublishSubject = PublishSubject.create();
    private final PublishSubject<MutableGroup> groupPublishSubject = PublishSubject.create();
    private final PublishSubject<MutableSubGroup> subGroupPublishSubject = PublishSubject.create();

    private MutableWashSurvey survey;
    private long currentGroupId;
    private long currentSubGroupId;
    private long currentQuestionId;

    public WashSurveyInteractorImpl(WashDataSource washDataSource) {
        this.washDataSource = washDataSource;
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
            survey.getGroups().forEach(group -> {
                initProgress(group);
                survey.getProgress().add(group.getProgress());
            });
        }
    }

    private void initProgress(MutableGroup group) {
        if (group.getSubGroups() != null) {
            group.getSubGroups().forEach(mutableSubGroup -> {
                initProgress(mutableSubGroup);
                group.getProgress().add(mutableSubGroup.getProgress());
            });
        }
    }

    private void initProgress(MutableSubGroup subGroup) {
        if (subGroup.getQuestions() != null) {
            subGroup.setProgress(new MutableProgress(
                    subGroup.getQuestions().size(),
                    (int) subGroup.getQuestions().parallelStream()
                            .filter(q -> q.getAnswer() != null && q.getAnswer().isAnsweredForQuestionType(q.getType()))
                            .count()
            ));
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
        return washDataSource.updateAnswer(answer, questionId)
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
        if (survey.getGroups() != null) {
            survey.getProgress().completed += survey.getGroups().parallelStream()
                    .filter(group -> group.getId() == groupId)
                    .findFirst()
                    .map(group -> findProgressDeltaAndNotify(answer, group, subGroupId, questionId))
                    .orElse(0);
            surveyPublishSubject.onNext(survey);
        }
    }

    private int findProgressDeltaAndNotify(MutableAnswer answer,
                                           MutableGroup group,
                                           long subGroupId,
                                           long questionId) {
        if (group.getSubGroups() == null) {
            return 0;
        }

        int delta = group.getSubGroups().parallelStream()
                .filter(subGroup -> subGroup.getId() == subGroupId)
                .findFirst()
                .map(subGroup -> findProgressDeltaAndNotify(answer, subGroup, questionId))
                .orElse(0);
        group.getProgress().completed += delta;
        groupPublishSubject.onNext(group);
        return delta;
    }

    private int findProgressDeltaAndNotify(MutableAnswer answer, MutableSubGroup subGroup, long questionId) {
        if (subGroup.getQuestions() == null) {
            return 0;
        }

        int oldCompleted = subGroup.getProgress().getCompleted();
        int completed = (int) subGroup.getQuestions().parallelStream()
                .peek(question -> {
                    if (question.getId() == questionId) {
                        question.setAnswer(answer);
                    }
                })
                .filter(question ->
                        question.getAnswer() != null && question.getAnswer().isAnsweredForQuestionType(question.getType())
                )
                .count();
        return completed - oldCompleted;
    }

    @Override
    public PublishSubject<Survey> getSurveyProgressSubject() {
        return surveyPublishSubject;
    }

    @Override
    public PublishSubject<MutableGroup> getGroupProgressSubject() {
        return groupPublishSubject;
    }

    @Override
    public PublishSubject<MutableSubGroup> getSubGroupProgressSubject() {
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
}
