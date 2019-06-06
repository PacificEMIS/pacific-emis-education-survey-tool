package fm.doe.national.accreditation.ui.questions;

import androidx.annotation.Nullable;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.InjectViewState;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import fm.doe.national.accreditation.R;
import fm.doe.national.accreditation.ui.navigation.concrete.ReportNavigationItem;
import fm.doe.national.accreditation_core.data.model.Answer;
import fm.doe.national.accreditation_core.data.model.SubCriteria;
import fm.doe.national.accreditation_core.data.model.mutable.MutableAnswer;
import fm.doe.national.accreditation_core.di.AccreditationCoreComponent;
import fm.doe.national.accreditation_core.interactors.AccreditationSurveyInteractor;
import fm.doe.national.cloud.di.CloudComponent;
import fm.doe.national.cloud.model.uploader.CloudUploader;
import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.di.CoreComponent;
import fm.doe.national.core.ui.screens.base.BasePresenter;
import fm.doe.national.survey_core.di.SurveyCoreComponent;
import fm.doe.national.survey_core.navigation.BuildableNavigationItem;
import fm.doe.national.survey_core.navigation.survey_navigator.SurveyNavigator;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class QuestionsPresenter extends BasePresenter<QuestionsView> {

    private final AccreditationSurveyInteractor accreditationSurveyInteractor;
    private final CloudUploader cloudUploader;
    private final SurveyNavigator navigator;
    private final long standardId;
    private final long categoryId;

    @Nullable
    private Question selectedQuestion;

    QuestionsPresenter(CoreComponent coreComponent,
                       CloudComponent cloudComponent,
                       SurveyCoreComponent surveyCoreComponent,
                       AccreditationCoreComponent accreditationCoreComponent,
                       long categoryId,
                       long standardId) {
        this.accreditationSurveyInteractor = accreditationCoreComponent.getAccreditationSurveyInteractor();
        this.cloudUploader = cloudComponent.getCloudUploader();
        this.navigator = surveyCoreComponent.getSurveyNavigator();
        this.standardId = standardId;
        this.categoryId = categoryId;
        loadQuestions();
        loadNavigation();
    }

    private void loadQuestions() {
        addDisposable(
                accreditationSurveyInteractor.requestCriterias(categoryId, standardId)
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

    private void loadNavigation() {
        BuildableNavigationItem navigationItem = navigator.getCurrentItem();
        QuestionsView view = getViewState();

        if (navigationItem.getNextItem() != null && navigationItem.getNextItem() instanceof ReportNavigationItem) {
            view.setNextButtonText(Text.from(R.string.button_complete));
            updateCompleteState(accreditationSurveyInteractor.getCurrentSurvey());
            subscribeOnProgressChanges();
        } else {
            view.setNextButtonText(Text.from(R.string.button_next));
        }

        view.setPrevButtonVisible(navigationItem.getPreviousItem() != null);
    }

    private void subscribeOnProgressChanges() {
        addDisposable(accreditationSurveyInteractor.getSurveyProgressSubject()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateCompleteState));
    }

    private void updateCompleteState(Survey survey) {
        boolean isFinished = survey.getProgress().isFinished();
        QuestionsView view = getViewState();
        view.setNextButtonEnabled(isFinished);
        view.setHintTextVisible(!isFinished);
    }

    void onCommentPressed(Question question) {
        selectedQuestion = question;
        getViewState().showCommentEditor(selectedQuestion.getSubCriteria());
    }

    void onPhotosPressed(Question question) {
        selectedQuestion = question;
        accreditationSurveyInteractor.setCurrentCategoryId(categoryId);
        accreditationSurveyInteractor.setCurrentStandardId(standardId);
        accreditationSurveyInteractor.setCurrentCriteriaId(selectedQuestion.getCriteria().getId());
        accreditationSurveyInteractor.setCurrentSubCriteriaId(selectedQuestion.getSubCriteria().getId());
        getViewState().navigateToPhotos();
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
        addDisposable(accreditationSurveyInteractor.updateAnswer(answer, categoryId, standardId, criteriaId, subCriteriaId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> cloudUploader.scheduleUploading(accreditationSurveyInteractor.getCurrentSurvey().getId()), this::handleError)
        );
    }

    void onPrevPressed() {
        navigator.selectPrevious();
    }

    void onNextPressed() {
        navigator.selectNext();
    }
}
