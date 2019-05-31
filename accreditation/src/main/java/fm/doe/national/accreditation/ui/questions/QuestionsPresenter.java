package fm.doe.national.accreditation.ui.questions;

import androidx.annotation.Nullable;

import com.omegar.mvp.InjectViewState;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import fm.doe.national.cloud.di.CloudComponent;
import fm.doe.national.cloud.model.uploader.CloudUploader;
import fm.doe.national.core.data.model.Answer;
import fm.doe.national.core.data.model.SubCriteria;
import fm.doe.national.core.data.model.mutable.MutableAnswer;
import fm.doe.national.core.di.CoreComponent;
import fm.doe.national.core.interactors.SurveyInteractor;
import fm.doe.national.core.ui.screens.base.BasePresenter;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class QuestionsPresenter extends BasePresenter<QuestionsView> {

    private final SurveyInteractor surveyInteractor;
    private final CloudUploader cloudUploader;
    private final long standardId;
    private final long categoryId;

    @Nullable
    private Question selectedQuestion;

    QuestionsPresenter(CoreComponent coreComponent,
                       CloudComponent cloudComponent,
                       long categoryId,
                       long standardId) {
        this.surveyInteractor = coreComponent.getSurveyInteractor();
        this.cloudUploader = cloudComponent.getCloudUploader();
        this.standardId = standardId;
        this.categoryId = categoryId;
        loadQuestions();
    }

    private void loadQuestions() {
        addDisposable(
                surveyInteractor.requestCriterias(categoryId, standardId)
                        .flatMap(criteriaList -> Single.fromCallable(() -> {
                            List<Question> questions = new ArrayList<>();
                            criteriaList.forEach(criteria -> {
                                        questions.add(new Question(criteria));
                                        questions.addAll(
                                                criteria.getSubCriterias()
                                                        .stream()
                                                        .map(subCriteria -> new Question(criteria, subCriteria))
                                                        .collect(Collectors.toList())
                                        );
                                    }
                            );
                            return questions;
                        }))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getViewState()::setQuestions, this::handleError)
        );
    }

    void onCommentPressed(Question question) {
        selectedQuestion = question;
        getViewState().showCommentEditor(selectedQuestion.getSubCriteria());
    }

    void onPhotosPressed(Question question) {
        selectedQuestion = question;
        getViewState().navigateToPhotos(
                categoryId,
                standardId,
                selectedQuestion.getCriteria().getId(),
                selectedQuestion.getSubCriteria().getId()
        );
    }

    void onAnswerChanged(Question updatedQuestion) {
        SubCriteria subCriteria = Objects.requireNonNull(updatedQuestion.getSubCriteria());
        update(subCriteria.getId(), updatedQuestion.getCriteria().getId(), subCriteria.getAnswer());
    }

    void onCommentEdit(String comment) {
        if (selectedQuestion == null) return;
        MutableAnswer answer = (MutableAnswer) selectedQuestion.getSubCriteria().getAnswer();
        answer.setComment(comment);
        update(
                selectedQuestion.getSubCriteria().getId(),
                selectedQuestion.getCriteria().getId(),
                selectedQuestion.getSubCriteria().getAnswer()
        );
        selectedQuestion = null;
    }

    private void update(long subCriteriaId, long criteriaId, Answer answer) {
        addDisposable(surveyInteractor.updateAnswer(answer, categoryId, standardId, criteriaId, subCriteriaId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> cloudUploader.scheduleUploading(surveyInteractor.getCurrentSurvey().getId()), this::handleError)
        );
    }
}
