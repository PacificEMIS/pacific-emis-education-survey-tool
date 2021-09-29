package org.pacific_emis.surveys.accreditation.ui.observation_log;

import androidx.annotation.NonNull;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.InjectViewState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.pacific_emis.surveys.accreditation.R;
import org.pacific_emis.surveys.accreditation.ui.navigation.concrete.ReportNavigationItem;
import org.pacific_emis.surveys.accreditation_core.data.model.ObservationLogRecord;
import org.pacific_emis.surveys.accreditation_core.data.model.mutable.MutableObservationLogRecord;
import org.pacific_emis.surveys.accreditation_core.di.AccreditationCoreComponent;
import org.pacific_emis.surveys.accreditation_core.interactors.AccreditationSurveyInteractor;
import org.pacific_emis.surveys.core.data.model.Survey;
import org.pacific_emis.surveys.core.preferences.entities.UploadState;
import org.pacific_emis.surveys.core.ui.screens.base.BasePresenter;
import org.pacific_emis.surveys.remote_storage.data.accessor.RemoteStorageAccessor;
import org.pacific_emis.surveys.remote_storage.data.storage.RemoteStorage;
import org.pacific_emis.surveys.remote_storage.di.RemoteStorageComponent;
import org.pacific_emis.surveys.survey_core.di.SurveyCoreComponent;
import org.pacific_emis.surveys.survey_core.navigation.BuildableNavigationItem;
import org.pacific_emis.surveys.survey_core.navigation.survey_navigator.SurveyNavigator;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class ObservationLogPresenter extends BasePresenter<ObservationLogView> {

    private final AccreditationSurveyInteractor accreditationSurveyInteractor;
    private final RemoteStorageAccessor remoteStorageAccessor;
    private final RemoteStorage remoteStorage;
    private final SurveyNavigator navigator;
    private final long categoryId;

    private List<MutableObservationLogRecord> records = Collections.emptyList();

    ObservationLogPresenter(RemoteStorageComponent remoteStorageComponent,
                            SurveyCoreComponent surveyCoreComponent,
                            AccreditationCoreComponent accreditationCoreComponent,
                            long categoryId) {
        this.accreditationSurveyInteractor = accreditationCoreComponent.getAccreditationSurveyInteractor();
        this.remoteStorageAccessor = remoteStorageComponent.getRemoteStorageAccessor();
        this.remoteStorage = remoteStorageComponent.getRemoteStorage();
        this.navigator = surveyCoreComponent.getSurveyNavigator();
        this.categoryId = categoryId;
        loadInfo();
        loadNavigation();
        updateUploadState();
        subscribeOnSurveyUploadState();
    }

    private void loadInfo() {
        getViewState().showWaiting();
        addDisposable(
                accreditationSurveyInteractor.requestLogRecords(categoryId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(() -> getViewState().hideWaiting())
                        .subscribe(savedItems -> {
                            records = savedItems;
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

    private void subscribeOnSurveyUploadState() {
        addDisposable(
                remoteStorage.getUploadStateObservable(accreditationSurveyInteractor.getCurrentSurvey().getId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::updateUploadState, this::handleError));
    }

    private void updateUploadState() {
        updateUploadState(accreditationSurveyInteractor.getCurrentUploadState());
    }

    private void updateUploadState(UploadState uploadState) {
        getViewState().setSurveyUploadState(uploadState);
    }

    private void updateSurvey() {
        addDisposable(
                accreditationSurveyInteractor.updateSurvey()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::updateUploadState, this::handleError)
        );
    }

    private void updateCompleteState(Survey survey) {
        boolean isFinished = survey.getProgress().isFinished();
        getViewState().setNextButtonEnabled(isFinished);
    }

    private void save(@NonNull ObservationLogRecord record) {
        addDisposable(accreditationSurveyInteractor.updateObservationLogRecord(record)
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

    public void onTeacherActionChanged(int position, String action) {
        if (position < 0 || position >= records.size()) {
            return;
        }
        MutableObservationLogRecord record = records.get(position);
        record.setTeacherActions(action);
        save(record);
    }

    public void onStudentsActionChanged(int position, String action) {
        if (position < 0 || position >= records.size()) {
            return;
        }
        MutableObservationLogRecord record = records.get(position);
        record.setStudentsActions(action);
        save(record);
    }

    public void onDeletePressed(int position) {
        if (position < 0 || position >= records.size()) {
            return;
        }
        MutableObservationLogRecord deletedRecord = records.get(position);
        records.remove(position);
        addDisposable(
                accreditationSurveyInteractor.deleteObservationLogRecord(deletedRecord.getId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::refreshRecords, this::handleError)
        );
    }

    public void onAddPressed() {
        addDisposable(
                accreditationSurveyInteractor.createEmptyLogRecord(categoryId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(createdRecord -> {
                            records.add(createdRecord);
                            refreshRecordsAfterAdding(createdRecord);
                        }, this::handleError)
        );
    }

    public void onTimePressed(int position) {
        if (position < 0 || position >= records.size()) {
            return;
        }
        MutableObservationLogRecord record = MutableObservationLogRecord.copyOf(records.get(position));
        getViewState().showTimePicker(record.getDate(), date -> {
            record.setDate(date);
            records.remove(position);
            records.add(record);
            refreshRecords();
            save(record);
        });
    }

    private void refreshRecordsAfterAdding(MutableObservationLogRecord addedRecord) {
        sortRecords();
        final int addedPosition = records.indexOf(addedRecord);
        getViewState().updateLogScrollingToPosition(new ArrayList<>(records), addedPosition); // clone list to have a different list instances in UI and in presenter
    }

    private void refreshRecords() {
        sortRecords();
        getViewState().updateLog(new ArrayList<>(records)); // clone list to have a different list instances in UI and in presenter
    }

    private void sortRecords() {
        Collections.sort(records, (lv, rv) -> {
            final Date leftDate = lv.getDate();
            final Date rightDate = rv.getDate();
            if (leftDate.equals(rightDate)) {
                return lv.getId() < rv.getId() ? -1 : 1; // it is not possible to have same ids
            } else {
                return leftDate.compareTo(rightDate);
            }
        });
    }
}
