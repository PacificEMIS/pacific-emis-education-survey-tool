package fm.doe.national.accreditation.ui.observation_info;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.InjectViewState;

import fm.doe.national.accreditation.R;
import fm.doe.national.accreditation.ui.navigation.concrete.ReportNavigationItem;
import fm.doe.national.accreditation_core.data.model.mutable.MutableObservationInfo;
import fm.doe.national.accreditation_core.di.AccreditationCoreComponent;
import fm.doe.national.accreditation_core.interactors.AccreditationSurveyInteractor;
import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.ui.screens.base.BasePresenter;
import fm.doe.national.remote_storage.data.accessor.RemoteStorageAccessor;
import fm.doe.national.remote_storage.di.RemoteStorageComponent;
import fm.doe.national.survey_core.di.SurveyCoreComponent;
import fm.doe.national.survey_core.navigation.BuildableNavigationItem;
import fm.doe.national.survey_core.navigation.survey_navigator.SurveyNavigator;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class ObservationInfoPresenter extends BasePresenter<ObservationInfoView> {
    private final AccreditationSurveyInteractor accreditationSurveyInteractor;
    private final RemoteStorageAccessor remoteStorageAccessor;
    private final SurveyNavigator navigator;
    private final long categoryId;

    private MutableObservationInfo observationInfo;

    ObservationInfoPresenter(RemoteStorageComponent remoteStorageComponent,
                       SurveyCoreComponent surveyCoreComponent,
                       AccreditationCoreComponent accreditationCoreComponent,
                       long categoryId) {
        this.accreditationSurveyInteractor = accreditationCoreComponent.getAccreditationSurveyInteractor();
        this.remoteStorageAccessor = remoteStorageComponent.getRemoteStorageAccessor();
        this.navigator = surveyCoreComponent.getSurveyNavigator();
        this.categoryId = categoryId;
        loadInfo();
        loadNavigation();
    }

    private void loadInfo() {
        addDisposable(
                accreditationSurveyInteractor.requestCategory(categoryId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(mutableCategory -> {
                            final MutableObservationInfo observationInfo = mutableCategory.getObservationInfo();
                            if (observationInfo == null) {
                                this.observationInfo = new MutableObservationInfo();
                            } else {
                                this.observationInfo = observationInfo;
                            }
                            onObservationInfoLoaded();
                        }, this::handleError)
        );
    }

    private void onObservationInfoLoaded() {
        ObservationInfoView view = getViewState();
        view.setTeacherName(observationInfo.getTeacherName());
        view.setGrade(observationInfo.getGrade());
        view.setTotalStudentsPresent(observationInfo.getTotalStudentsPresent());
        view.setSubject(observationInfo.getSubject());
        view.setDate(observationInfo.getDate());
    }

    private void loadNavigation() {
        BuildableNavigationItem navigationItem = navigator.getCurrentItem();
        ObservationInfoView view = getViewState();

        if (navigationItem.getNextItem() != null && navigationItem.getNextItem() instanceof ReportNavigationItem) {
            view.setNextButtonText(Text.from(R.string.button_complete));
            updateCompleteState(accreditationSurveyInteractor.getCurrentSurvey());
        } else {
            view.setNextButtonText(Text.from(R.string.button_next));
        }

        view.setPrevButtonVisible(navigationItem.getPreviousItem() != null);
    }

    private void updateCompleteState(Survey survey) {
        boolean isFinished = survey.getProgress().isFinished();
        ObservationInfoView view = getViewState();
        view.setNextButtonEnabled(isFinished);
    }

//    void onAnswerChanged(Question updatedQuestion) {
//        SubCriteria subCriteria = Objects.requireNonNull(updatedQuestion.getSubCriteria());
//        update(subCriteria.getId(), updatedQuestion.getCriteria().getId(), subCriteria.getAnswer());
//    }
//
//    private void update(long subCriteriaId, long criteriaId, Answer answer) {
//        addDisposable(accreditationSurveyInteractor.updateAnswer(answer, categoryId, standardId, criteriaId, subCriteriaId)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        () -> remoteStorageAccessor.scheduleUploading(accreditationSurveyInteractor.getCurrentSurvey().getId()),
//                        this::handleError
//                )
//        );
//    }

    void onPrevPressed() {
        navigator.selectPrevious();
    }

    void onNextPressed() {
        Runnable navigateToNext = navigator::selectNext;
        if (navigator.getCurrentItem().getNextItem() instanceof ReportNavigationItem) {
            addDisposable(
                    accreditationSurveyInteractor.completeSurveyIfNeed()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(navigateToNext::run, this::handleError)
            );
        } else {
            navigateToNext.run();
        }
    }
}
