package org.pacific_emis.surveys.accreditation.ui.observation_info;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.InjectViewState;

import org.pacific_emis.surveys.accreditation.R;
import org.pacific_emis.surveys.accreditation.ui.navigation.concrete.ReportNavigationItem;
import org.pacific_emis.surveys.accreditation_core.data.model.ObservationInfo;
import org.pacific_emis.surveys.accreditation_core.data.model.mutable.MutableObservationInfo;
import org.pacific_emis.surveys.accreditation_core.di.AccreditationCoreComponent;
import org.pacific_emis.surveys.accreditation_core.interactors.AccreditationSurveyInteractor;
import org.pacific_emis.surveys.core.data.model.Subject;
import org.pacific_emis.surveys.core.data.model.Survey;
import org.pacific_emis.surveys.core.data.model.Teacher;
import org.pacific_emis.surveys.core.di.CoreComponent;
import org.pacific_emis.surveys.core.di.CoreComponentInjector;
import org.pacific_emis.surveys.core.preferences.LocalSettings;
import org.pacific_emis.surveys.core.preferences.entities.UploadState;
import org.pacific_emis.surveys.core.ui.screens.base.BasePresenter;
import org.pacific_emis.surveys.remote_storage.data.accessor.RemoteStorageAccessor;
import org.pacific_emis.surveys.remote_storage.data.storage.RemoteStorage;
import org.pacific_emis.surveys.remote_storage.di.RemoteStorageComponent;
import org.pacific_emis.surveys.survey_core.di.SurveyCoreComponent;
import org.pacific_emis.surveys.survey_core.navigation.BuildableNavigationItem;
import org.pacific_emis.surveys.survey_core.navigation.survey_navigator.SurveyNavigator;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class ObservationInfoPresenter extends BasePresenter<ObservationInfoView> {

    private final static List<String> POSSIBLE_GRADES = Arrays.asList(
            "Grade ECE",
            "Grade 1",
            "Grade 2",
            "Grade 3",
            "Grade 4",
            "Grade 5",
            "Grade 6",
            "Grade 7",
            "Grade 8",
            "Grade 9",
            "Grade 10",
            "Grade 11",
            "Grade 12"
    );

    private final AccreditationSurveyInteractor accreditationSurveyInteractor;
    private final RemoteStorageAccessor remoteStorageAccessor;
    private final RemoteStorage remoteStorage;
    private final SurveyNavigator navigator;
    private final LocalSettings localSettings;
    private final long categoryId;

    private MutableObservationInfo observationInfo;

    ObservationInfoPresenter(RemoteStorageComponent remoteStorageComponent,
                             SurveyCoreComponent surveyCoreComponent,
                             CoreComponent coreComponent,
                             AccreditationCoreComponent accreditationCoreComponent,
                             long categoryId) {
        this.accreditationSurveyInteractor = accreditationCoreComponent.getAccreditationSurveyInteractor();
        this.remoteStorageAccessor = remoteStorageComponent.getRemoteStorageAccessor();
        this.remoteStorage = remoteStorageComponent.getRemoteStorage();
        this.navigator = surveyCoreComponent.getSurveyNavigator();
        this.localSettings = coreComponent.getLocalSettings();
        this.categoryId = categoryId;
        loadInfo();
        loadNavigation();
        onUploadState();
        subscribeOnSurveyUploadState();
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
        addDisposable(
                accreditationSurveyInteractor.loadTeachers(localSettings.getCurrentAppRegion())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> getViewState().showWaiting())
                        .doFinally(() -> getViewState().hideWaiting())
                        .subscribe(teachers -> {
                            getViewState().setTeachersToAutocompleteField(teachers);
                        }, this::handleError)
        );
        addDisposable(
                accreditationSurveyInteractor.loadSubjects(localSettings.getCurrentAppRegion())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> getViewState().showWaiting())
                        .doFinally(() -> getViewState().hideWaiting())
                        .subscribe(subjects -> {
                            getViewState().setSubjectsToAutocompleteField(subjects);
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

    private void subscribeOnSurveyUploadState() {
        addDisposable(
                remoteStorage.updateSurveyUploadState()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::onUploadStateChanged, this::handleError));
    }

    private void onUploadStateChanged(UploadState remoteState) {
        UploadState surveyState = accreditationSurveyInteractor.getCurrentUploadState();

        if (surveyState == UploadState.NOT_UPLOAD && remoteState == UploadState.IN_PROGRESS) {
            accreditationSurveyInteractor.setCurrentUploadState(remoteState);
            updateSurvey();
        }

        if (surveyState == UploadState.IN_PROGRESS && remoteState == UploadState.SUCCESSFULLY) {
            accreditationSurveyInteractor.setCurrentUploadState(remoteState);
            updateSurvey();
        }

        if (surveyState == UploadState.SUCCESSFULLY && remoteState == UploadState.IN_PROGRESS) {
            accreditationSurveyInteractor.setCurrentUploadState(remoteState);
            updateSurvey();
        }
    }

    private void onUploadState() {
        getViewState().setSurveyUploadState(accreditationSurveyInteractor.getCurrentUploadState());
    }

    private void updateSurvey() {
        addDisposable(
                accreditationSurveyInteractor.updateSurvey()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::onUploadState, this::handleError)
        );
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
        accreditationSurveyInteractor.setCurrentUploadState(UploadState.NOT_UPLOAD);
        updateSurvey();
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

    public void onTeacherChanged(Teacher teacher) {
        observationInfo.setTeacherName(teacher.getName());
        observationInfo.setTeacherId(teacher.getId());
        save(observationInfo);
    }

    public void onTeacherChanged(String teacher) {
        if (!teacher.equals(observationInfo.getTeacherName())) {
            observationInfo.setTeacherName(teacher);
            save(observationInfo);
        }
    }

    public void onTotalStudentsChanged(Integer totalStudents) {
        observationInfo.setTotalStudentsPresent(totalStudents);
        save(observationInfo);
    }

    public void onSubjectChanged(Subject subject) {
        observationInfo.setSubject(subject.getName());
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
