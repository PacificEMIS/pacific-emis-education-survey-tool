package org.pacific_emis.surveys.ui.screens.logs;

import com.omegar.mvp.InjectViewState;

import org.pacific_emis.surveys.app_support.MicronesiaApplication;
import org.pacific_emis.surveys.core.data.data_repository.LogsRepository;
import org.pacific_emis.surveys.core.data.local_data_source.DataSource;
import org.pacific_emis.surveys.core.data.model.Survey;
import org.pacific_emis.surveys.core.preferences.LocalSettings;
import org.pacific_emis.surveys.core.preferences.entities.LogAction;
import org.pacific_emis.surveys.core.ui.screens.base.BasePresenter;
import org.pacific_emis.surveys.remote_storage.data.storage.RemoteStorage;

import java.util.Collections;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class LogsPresenter extends BasePresenter<LogsView> {

    private final DataSource dataSource = MicronesiaApplication.getInjection().getDataSourceComponent().getDataRepository();
    private final LocalSettings localSettings = MicronesiaApplication.getInjection().getCoreComponent().getLocalSettings();
    private final LogsRepository logsRepository = MicronesiaApplication.getInjection().getDataSourceComponent().getLogsRepository();
    private final RemoteStorage remoteStorage = MicronesiaApplication.getInjection().getRemoteStorageComponent().getRemoteStorage();

    LogsPresenter() {
        checkDriveChanges();
        loadLogs();
    }

    private void checkDriveChanges() {
        addDisposable(logsRepository.loadAllSurveys(localSettings.getCurrentAppRegion())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(this::getDriveChanges, this::handleError));
    }

    private void getDriveChanges(List<Survey> surveys) {
        addDisposable(
                remoteStorage.driveFileChanges(surveys, localSettings.getDrivePageToken())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::deletedDriveSurveys, Throwable::printStackTrace));
    }

    private void deletedDriveSurveys(List<Survey> result) {
        if (!result.isEmpty()) {
            result.forEach(this::deleteSurvey);
        }
        fetchNewPageToken();
    }

    private void fetchNewPageToken() {
        addDisposable(
                remoteStorage.fetchStartPageToken()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(localSettings::setDrivePageToken, Throwable::printStackTrace));
    }

    private void deleteSurvey(Survey survey) {
        addDisposable(logsRepository.deleteSurvey(survey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(() -> {
                    saveDeletedSurveyInfo(survey);
                    loadLogs();
                }, this::handleError));
    }

    private void saveDeletedSurveyInfo(Survey survey) {
        addDisposable(dataSource.saveLogInfo(survey, LogAction.DELETED)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(() -> {}, this::handleError));
    }

    private void loadLogs() {
        addDisposable(dataSource.loadLogs(localSettings.getCurrentAppRegion())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .map(logs -> {
                    Collections.reverse(logs);
                    return logs;
                })
                .subscribe(logs -> {
                    if (!logs.isEmpty()) {
                        getViewState().setLogs(logs);
                    }
                }, this::handleError));
    }

}
