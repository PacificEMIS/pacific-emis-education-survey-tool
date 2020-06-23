package fm.doe.national.accreditation.ui.observation_log;

import androidx.annotation.NonNull;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.InjectViewState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import fm.doe.national.accreditation.R;
import fm.doe.national.accreditation.ui.navigation.concrete.ReportNavigationItem;
import fm.doe.national.accreditation_core.data.model.ObservationInfo;
import fm.doe.national.accreditation_core.data.model.ObservationLogRecord;
import fm.doe.national.accreditation_core.data.model.mutable.MutableObservationLogRecord;
import fm.doe.national.accreditation_core.di.AccreditationCoreComponent;
import fm.doe.national.accreditation_core.interactors.AccreditationSurveyInteractor;
import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.ui.screens.base.BasePresenter;
import fm.doe.national.remote_storage.data.accessor.RemoteStorageAccessor;
import fm.doe.national.remote_storage.di.RemoteStorageComponent;
import fm.doe.national.survey_core.di.SurveyCoreComponent;
import fm.doe.national.survey_core.navigation.BuildableNavigationItem;
import fm.doe.national.survey_core.navigation.survey_navigator.SurveyNavigator;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class ObservationLogPresenter extends BasePresenter<ObservationLogView> {

    private final AccreditationSurveyInteractor accreditationSurveyInteractor;
    private final RemoteStorageAccessor remoteStorageAccessor;
    private final SurveyNavigator navigator;
    private final long categoryId;

    private List<MutableObservationLogRecord> records = Collections.emptyList();
    private long tmpIdGeg = 0;

    ObservationLogPresenter(RemoteStorageComponent remoteStorageComponent,
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
        List<ObservationLogRecord> defList = Collections.emptyList();
        addDisposable(
                Single.fromCallable(() -> defList)
//                accreditationSurveyInteractor.requestCategory(categoryId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(savedItems -> {
                            records = savedItems.stream()
                                    .map(MutableObservationLogRecord::from)
                                    .collect(Collectors.toList());
                            refreshRecords();
                        }, this::handleError)
        );
    }

    private void loadNavigation() {
        BuildableNavigationItem navigationItem = navigator.getCurrentItem();
        ObservationLogView view = getViewState();

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

    public void onTeacherActionChanged(int position, String action) {

    }

    public void onStudentsActionChanged(int position, String action) {

    }

    public void onDeletePressed(int position) {
        if (position < records.size()) {
            // TODO: delete from db
            records.remove(position);
            refreshRecords();
        }
    }

    public void onAddPressed() {
        // TODO: save to db to generate id
        records.add(new MutableObservationLogRecord(tmpIdGeg++, new Date()));
        refreshRecords();
    }

    public void onTimePressed(int position) {

    }

    private void refreshRecords() {
        Collections.sort(records, (lv, rv) -> {
            final Date leftDate = lv.getDate();
            final Date rightDate = rv.getDate();
            if (leftDate.equals(rightDate)) {
                return lv.getId() < rv.getId() ? -1 : 1; // it is not possible to have same ids
            } else {
                return leftDate.compareTo(rightDate);
            }
        });
        getViewState().updateLog(new ArrayList<>(records)); // clone list to have a different list instances in UI and in presenter
    }
}
