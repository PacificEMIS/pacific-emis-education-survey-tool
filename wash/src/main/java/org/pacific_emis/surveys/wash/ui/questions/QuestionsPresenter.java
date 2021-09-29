package org.pacific_emis.surveys.wash.ui.questions;

import androidx.annotation.Nullable;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.InjectViewState;

import org.pacific_emis.surveys.core.data.model.Survey;
import org.pacific_emis.surveys.core.preferences.entities.UploadState;
import org.pacific_emis.surveys.core.ui.screens.base.BasePresenter;
import org.pacific_emis.surveys.remote_storage.data.accessor.RemoteStorageAccessor;
import org.pacific_emis.surveys.remote_storage.data.storage.RemoteStorage;
import org.pacific_emis.surveys.remote_storage.di.RemoteStorageComponent;
import org.pacific_emis.surveys.survey_core.di.SurveyCoreComponent;
import org.pacific_emis.surveys.survey_core.navigation.BuildableNavigationItem;
import org.pacific_emis.surveys.survey_core.navigation.survey_navigator.SurveyNavigator;
import org.pacific_emis.surveys.wash.R;
import org.pacific_emis.surveys.wash_core.data.model.Answer;
import org.pacific_emis.surveys.wash_core.data.model.Location;
import org.pacific_emis.surveys.wash_core.data.model.mutable.MutableAnswer;
import org.pacific_emis.surveys.wash_core.data.model.mutable.MutableQuestion;
import org.pacific_emis.surveys.wash_core.di.WashCoreComponent;
import org.pacific_emis.surveys.wash_core.interactors.WashSurveyInteractor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class QuestionsPresenter extends BasePresenter<QuestionsView> {

    private final WashSurveyInteractor washSurveyInteractor;
    private final RemoteStorageAccessor remoteStorageAccessor;
    private final RemoteStorage remoteStorage;
    private final SurveyNavigator navigator;
    private final long subGroupId;
    private final long groupId;

    @Nullable
    private MutableQuestion selectedQuestion;
    private int selectedQuestionPosition;

    QuestionsPresenter(RemoteStorageComponent remoteStorageComponent,
                       SurveyCoreComponent surveyCoreComponent,
                       WashCoreComponent washCoreComponent,
                       long groupId,
                       long subGroupId) {
        this.washSurveyInteractor = washCoreComponent.getWashSurveyInteractor();
        this.remoteStorageAccessor = remoteStorageComponent.getRemoteStorageAccessor();
        this.remoteStorage = remoteStorageComponent.getRemoteStorage();
        this.navigator = surveyCoreComponent.getSurveyNavigator();
        this.subGroupId = subGroupId;
        this.groupId = groupId;
        loadQuestions();
        loadNavigation();
        onUploadState();
        subscribeOnSurveyUploadState();
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

        if (navigationItem.getNextItem() == null) {
            view.setNextButtonText(Text.from(R.string.button_complete));
            updateCompleteState(washSurveyInteractor.getCurrentSurvey());
            subscribeOnProgressChanges();
        } else {
            view.setNextButtonText(Text.from(R.string.button_next));
        }

        view.setPrevButtonVisible(navigationItem.getPreviousItem() != null);
    }

    private void subscribeOnProgressChanges() {
        addDisposable(washSurveyInteractor.getSurveyProgressObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateCompleteState));
    }

    private void subscribeOnSurveyUploadState() {
        addDisposable(
                remoteStorage.updateSurveyUploadState()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::onUploadStateChanged, this::handleError));
    }

    private void onUploadStateChanged(UploadState remoteState) {
        UploadState surveyState = washSurveyInteractor.getCurrentUploadState();

        if (surveyState == UploadState.NOT_UPLOAD && remoteState == UploadState.IN_PROGRESS) {
            washSurveyInteractor.setCurrentUploadState(remoteState);
            updateSurvey();
        }

        if (surveyState == UploadState.IN_PROGRESS && remoteState == UploadState.SUCCESSFULLY) {
            washSurveyInteractor.setCurrentUploadState(remoteState);
            updateSurvey();
        }

        if (surveyState == UploadState.SUCCESSFULLY && remoteState == UploadState.IN_PROGRESS) {
            washSurveyInteractor.setCurrentUploadState(remoteState);
            updateSurvey();
        }
    }

    private void onUploadState() {
        getViewState().setSurveyUploadState(washSurveyInteractor.getCurrentUploadState());
    }

    private void updateSurvey() {
        addDisposable(
                washSurveyInteractor.updateSurvey()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::onUploadState, this::handleError)
        );
    }

    private void updateCompleteState(Survey survey) {
        boolean isFinished = survey.getProgress().isFinished();
        QuestionsView view = getViewState();
        view.setNextButtonEnabled(isFinished);
        view.setHintTextVisible(!isFinished);
    }

    void onCommentPressed(MutableQuestion question, int position) {
        selectedQuestion = question;
        selectedQuestionPosition = position;
        getViewState().showCommentEditor(selectedQuestion);
    }

    void onPhotosPressed(MutableQuestion question, int position) {
        selectedQuestion = question;
        selectedQuestionPosition = position;
        washSurveyInteractor.setCurrentGroupId(groupId);
        washSurveyInteractor.setCurrentSubGroupId(subGroupId);
        washSurveyInteractor.setCurrentQuestionId(selectedQuestion.getId());
        getViewState().navigateToPhotos();
    }

    void onAnswerChanged(MutableQuestion updatedQuestion) {
        update(updatedQuestion.getId(), updatedQuestion.getAnswer());
    }

    void onCommentEdit(String comment) {
        if (selectedQuestion == null) {
            return;
        }

        MutableAnswer answer = selectedQuestion.getAnswer();
        answer.setComment(comment);
        update(selectedQuestion.getId(), answer);
        selectedQuestion = null;
        getViewState().refreshQuestionAtPosition(selectedQuestionPosition);
    }

    private void update(long questionId, Answer answer) {
        addDisposable(washSurveyInteractor.updateAnswer(answer, groupId, subGroupId, questionId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> remoteStorageAccessor.scheduleUploading(washSurveyInteractor.getCurrentSurvey().getId()), this::handleError)
        );
        washSurveyInteractor.setCurrentUploadState(UploadState.NOT_UPLOAD);
        updateSurvey();
    }

    void onPrevPressed() {
        navigator.selectPrevious();
    }

    void onNextPressed() {
        if (navigator.getCurrentItem().getNextItem() != null) {
            navigator.selectNext();
        } else {
            navigator.close();
        }
    }

    void onLocationChanged(Location location, MutableQuestion question) {
        MutableAnswer answer = question.getAnswer();

        if (answer != null) {
            answer.setLocation(location);
            onAnswerChanged(question);
        }
    }

    public void onDeleteCommentPressed(MutableQuestion question, int position) {
        MutableAnswer answer = question.getAnswer();
        answer.setComment(null);
        update(question.getId(), answer);
        getViewState().refreshQuestionAtPosition(position);
    }

    public void onReturnFromPhotos() {
        getViewState().refreshQuestionAtPosition(selectedQuestionPosition);
    }
}
