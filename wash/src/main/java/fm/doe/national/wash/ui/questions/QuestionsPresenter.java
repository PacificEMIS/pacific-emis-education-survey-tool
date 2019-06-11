package fm.doe.national.wash.ui.questions;

import androidx.annotation.Nullable;

import com.omegar.mvp.InjectViewState;

import fm.doe.national.cloud.di.CloudComponent;
import fm.doe.national.cloud.model.uploader.CloudUploader;
import fm.doe.national.core.di.CoreComponent;
import fm.doe.national.core.ui.screens.base.BasePresenter;
import fm.doe.national.survey_core.di.SurveyCoreComponent;
import fm.doe.national.survey_core.navigation.BuildableNavigationItem;
import fm.doe.national.survey_core.navigation.survey_navigator.SurveyNavigator;
import fm.doe.national.wash_core.data.model.Answer;
import fm.doe.national.wash_core.data.model.Location;
import fm.doe.national.wash_core.data.model.mutable.MutableAnswer;
import fm.doe.national.wash_core.data.model.mutable.MutableQuestion;
import fm.doe.national.wash_core.di.WashCoreComponent;
import fm.doe.national.wash_core.interactors.WashSurveyInteractor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class QuestionsPresenter extends BasePresenter<QuestionsView> {

    private final WashSurveyInteractor washSurveyInteractor;
    private final CloudUploader cloudUploader;
    private final SurveyNavigator navigator;
    private final long subGroupId;
    private final long groupId;

    @Nullable
    private MutableQuestion selectedQuestion;

    QuestionsPresenter(CoreComponent coreComponent,
                       CloudComponent cloudComponent,
                       SurveyCoreComponent surveyCoreComponent,
                       WashCoreComponent washCoreComponent,
                       long groupId,
                       long subGroupId) {
        this.washSurveyInteractor = washCoreComponent.getWashSurveyInteractor();
        this.cloudUploader = cloudComponent.getCloudUploader();
        this.navigator = surveyCoreComponent.getSurveyNavigator();
        this.subGroupId = subGroupId;
        this.groupId = groupId;
        loadQuestions();
        loadNavigation();
    }

    private void loadQuestions() {
        addDisposable(
                washSurveyInteractor.requestQuestions(groupId, subGroupId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getViewState()::setQuestions, this::handleError)
        );
    }

    private void loadNavigation() {
        BuildableNavigationItem navigationItem = navigator.getCurrentItem();
        QuestionsView view = getViewState();
        view.setNextButtonVisible(navigationItem.getNextItem() != null);
        view.setPrevButtonVisible(navigationItem.getPreviousItem() != null);
    }

    void onCommentPressed(MutableQuestion question) {
        selectedQuestion = question;
        getViewState().showCommentEditor(selectedQuestion);
    }

    void onPhotosPressed(MutableQuestion question) {
        selectedQuestion = question;
        washSurveyInteractor.setCurrentGroupId(groupId);
        washSurveyInteractor.setCurrentSubGroupId(subGroupId);
        washSurveyInteractor.setCurrentQuestionId(selectedQuestion.getId());
        getViewState().navigateToPhotos();
    }

    void onAnswerChanged(MutableQuestion updatedQuestion) {
        update(updatedQuestion.getId(), updatedQuestion.getAnswer());
    }

    void onCommentEdit(String comment) {
        if (selectedQuestion == null) return;
        MutableAnswer answer = selectedQuestion.getAnswer();
        answer.setComment(comment);
        update(selectedQuestion.getId(), answer);
        selectedQuestion = null;
    }

    private void update(long questionId, Answer answer) {
        addDisposable(washSurveyInteractor.updateAnswer(answer, groupId, subGroupId, questionId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> cloudUploader.scheduleUploading(washSurveyInteractor.getCurrentSurvey().getId()), this::handleError)
        );
    }

    void onPrevPressed() {
        navigator.selectPrevious();
    }

    void onNextPressed() {
        navigator.selectNext();
    }

    void onLocationChanged(Location location, MutableQuestion question) {
        MutableAnswer answer = question.getAnswer();

        if (answer != null) {
            answer.setLocation(location);
            onAnswerChanged(question);
        }
    }
}
