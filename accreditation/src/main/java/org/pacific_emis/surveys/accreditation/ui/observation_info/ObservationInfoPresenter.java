package org.pacific_emis.surveys.accreditation.ui.observation_info;

import androidx.annotation.NonNull;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.InjectViewState;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.pacific_emis.surveys.accreditation.R;
import org.pacific_emis.surveys.accreditation.ui.navigation.concrete.ReportNavigationItem;
import org.pacific_emis.surveys.accreditation_core.data.model.ObservationInfo;
import org.pacific_emis.surveys.accreditation_core.data.model.mutable.MutableObservationInfo;
import org.pacific_emis.surveys.accreditation_core.di.AccreditationCoreComponent;
import org.pacific_emis.surveys.accreditation_core.interactors.AccreditationSurveyInteractor;
import org.pacific_emis.surveys.core.data.model.Survey;
import org.pacific_emis.surveys.core.ui.screens.base.BasePresenter;
import org.pacific_emis.surveys.remote_storage.data.accessor.RemoteStorageAccessor;
import org.pacific_emis.surveys.remote_storage.di.RemoteStorageComponent;
import org.pacific_emis.surveys.survey_core.di.SurveyCoreComponent;
import org.pacific_emis.surveys.survey_core.navigation.BuildableNavigationItem;
import org.pacific_emis.surveys.survey_core.navigation.survey_navigator.SurveyNavigator;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class ObservationInfoPresenter extends BasePresenter<ObservationInfoView> {

    private final static List<String> POSSIBLE_GRADES = Arrays.asList(
            "Grade 1-10",
            "Grade 1-4",
            "Grade 1-6",
            "Grade 1-7",
            "Grade 1-8",
            "Grade 9-11",
            "Grade 9-12",
            "Grade ECE-10",
            "Grade ECE-12",
            "Grade ECE-5",
            "Grade ECE-8",
            "Grade 1-5",
            "Grade 5-8"
    );

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

        Date savedDate = observationInfo.getDate();
        if (savedDate == null) {
            savedDate = new Date();
            observationInfo.setDate(savedDate);
        }
        view.setDate(savedDate);
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
        getViewState().setNextButtonEnabled(isFinished);
    }

    private void save(@NonNull ObservationInfo observationInfo) {
        addDisposable(accreditationSurveyInteractor.updateClassroomObservationInfo(observationInfo, categoryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> remoteStorageAccessor.scheduleUploading(accreditationSurveyInteractor.getCurrentSurvey().getId()),
                        this::handleError
                )
        );
    }

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

    public void onTeacherNameChanged(String teacherName) {
        observationInfo.setTeacherName(teacherName);
        save(observationInfo);
    }

    public void onTotalStudentsChanged(Integer totalStudents) {
        observationInfo.setTotalStudentsPresent(totalStudents);
        save(observationInfo);
    }

    public void onSubjectChanged(String subject) {
        observationInfo.setSubject(subject);
        save(observationInfo);
    }

    public void onDateTimePressed() {
        final Date savedDate = observationInfo.getDate();
        if (savedDate != null) {
            getViewState().showDateTimePicker(observationInfo.getDate(), date -> {
                observationInfo.setDate(date);
                getViewState().setDate(date);
                save(observationInfo);
            });
        }
    }

    public void onGradePressed() {
        getViewState().showGradeSelector(POSSIBLE_GRADES, selectedGrade -> {
            observationInfo.setGrade(selectedGrade);
            getViewState().setGrade(selectedGrade);
            save(observationInfo);
        });
    }
}
