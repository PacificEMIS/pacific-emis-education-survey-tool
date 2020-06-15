package fm.doe.national.accreditation.ui.observation_info;

import android.util.Log;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.InjectViewState;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import fm.doe.national.accreditation.R;
import fm.doe.national.accreditation.ui.navigation.concrete.ReportNavigationItem;
import fm.doe.national.accreditation_core.data.model.mutable.MutableObservationInfo;
import fm.doe.national.accreditation_core.di.AccreditationCoreComponent;
import fm.doe.national.accreditation_core.interactors.AccreditationSurveyInteractor;
import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.ui.screens.base.BasePresenter;
import fm.doe.national.core.utils.RxNullableObject;
import fm.doe.national.remote_storage.data.accessor.RemoteStorageAccessor;
import fm.doe.national.remote_storage.di.RemoteStorageComponent;
import fm.doe.national.survey_core.di.SurveyCoreComponent;
import fm.doe.national.survey_core.navigation.BuildableNavigationItem;
import fm.doe.national.survey_core.navigation.survey_navigator.SurveyNavigator;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

@InjectViewState
public class ObservationInfoPresenter extends BasePresenter<ObservationInfoView> {
    private final AccreditationSurveyInteractor accreditationSurveyInteractor;
    private final RemoteStorageAccessor remoteStorageAccessor;
    private final SurveyNavigator navigator;
    private final long categoryId;

    private MutableObservationInfo observationInfo;

    private PublishSubject<RxNullableObject<String>> teacherNameSubject = PublishSubject.create();
    private PublishSubject<RxNullableObject<Integer>> totalStudentsSubject = PublishSubject.create();
    private PublishSubject<RxNullableObject<String>> classThemeSubject = PublishSubject.create();
    private PublishSubject<MutableObservationInfo> infoChangedSubject = PublishSubject.create();

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
        setupViewInteraction();
        setupSaving();
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
            observationInfo.setDate(new Date());
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
        ObservationInfoView view = getViewState();
        view.setNextButtonEnabled(isFinished);
    }

    private void setupViewInteraction() {
        addDisposable(
                teacherNameSubject
                        .throttleLast(1, TimeUnit.SECONDS)
                        .subscribe(name -> {
                            observationInfo.setTeacherName(name.object);
                            infoChangedSubject.onNext(observationInfo);
                        }, this::handleError)
        );
        addDisposable(
                totalStudentsSubject
                        .throttleLast(1, TimeUnit.SECONDS)
                        .subscribe(count -> {
                            observationInfo.setTotalStudentsPresent(count.object);
                            infoChangedSubject.onNext(observationInfo);
                        }, this::handleError)
        );
        addDisposable(
                classThemeSubject
                        .throttleLast(1, TimeUnit.SECONDS)
                        .subscribe(classSubject -> {
                            observationInfo.setSubject(classSubject.object);
                            infoChangedSubject.onNext(observationInfo);
                        }, this::handleError)
        );
    }

    private void setupSaving() {
        addDisposable(
                infoChangedSubject
                        .throttleLast(1, TimeUnit.SECONDS)
                        .subscribe(observationInfo -> {
                            logState();
                        }, this::handleError)
        );
    }

    private void logState() {
        Log.d("RX", "============SAVE CALLED================ \n"
                + "logState: \n"
                + "teacherName = " + (observationInfo.getTeacherName() == null ? "null" : observationInfo.getTeacherName()) + "\n"
                + "grade = " + (observationInfo.getGrade() == null ? "null" : observationInfo.getGrade()) + "\n"
                + "totalStudentsPresent = " + (observationInfo.getTotalStudentsPresent() == null ? "null" : observationInfo.getTotalStudentsPresent().toString()) + "\n"
                + "subject = " + (observationInfo.getSubject() == null ? "null" : observationInfo.getSubject()) + "\n"
                + "date = " + (observationInfo.getDate() == null ? "null" : observationInfo.getDate().toString()) + "\n"
                + "============================ \n"
        );
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

    public void onTeacherNameChanged(String teacherName) {
        teacherNameSubject.onNext(RxNullableObject.wrap(teacherName));
    }

    public void onGradeChanged(String grade) {
        observationInfo.setGrade(grade);
        infoChangedSubject.onNext(observationInfo);
    }

    public void onTotalStudentsChanged(Integer totalStudents) {
        totalStudentsSubject.onNext(RxNullableObject.wrap(totalStudents));
    }

    public void onSubjectChanged(String subject) {
        classThemeSubject.onNext(RxNullableObject.wrap(subject));
    }

    public void onDateTimePressed() {
        final Date savedDate = observationInfo.getDate();
        if (savedDate != null) {
            getViewState().showDateTimePicker(observationInfo.getDate(), date -> {
                observationInfo.setDate(date);
                getViewState().setDate(date);
                infoChangedSubject.onNext(observationInfo);
            });
        }
    }
}
