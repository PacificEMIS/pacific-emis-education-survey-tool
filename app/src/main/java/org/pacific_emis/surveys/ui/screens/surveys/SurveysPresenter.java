package org.pacific_emis.surveys.ui.screens.surveys;

import android.content.Context;
import android.net.Uri;
import android.util.Pair;

import androidx.annotation.NonNull;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.InjectViewState;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.pacific_emis.surveys.R;
import org.pacific_emis.surveys.accreditation_core.data.model.AccreditationSurvey;
import org.pacific_emis.surveys.app_support.MicronesiaApplication;
import org.pacific_emis.surveys.core.data.data_repository.LogsRepository;
import org.pacific_emis.surveys.core.data.local_data_source.DataSource;
import org.pacific_emis.surveys.core.data.exceptions.NotImplementedException;
import org.pacific_emis.surveys.core.data.files.FilesRepository;
import org.pacific_emis.surveys.core.data.model.Survey;
import org.pacific_emis.surveys.core.data.model.SurveyState;
import org.pacific_emis.surveys.core.data.model.mutable.MutableSurvey;
import org.pacific_emis.surveys.core.domain.SurveyInteractor;
import org.pacific_emis.surveys.core.preferences.LocalSettings;
import org.pacific_emis.surveys.core.preferences.entities.LogAction;
import org.pacific_emis.surveys.core.preferences.entities.UploadState;
import org.pacific_emis.surveys.domain.SettingsInteractor;
import org.pacific_emis.surveys.offline_sync.domain.OfflineSyncUseCase;
import org.pacific_emis.surveys.offline_sync.ui.base.BaseBluetoothPresenter;
import org.pacific_emis.surveys.remote_storage.data.accessor.RemoteStorageAccessor;
import org.pacific_emis.surveys.remote_storage.data.model.ExportType;
import org.pacific_emis.surveys.remote_storage.data.storage.RemoteStorage;
import org.pacific_emis.surveys.remote_storage.utils.RemoteStorageUtils;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class SurveysPresenter extends BaseBluetoothPresenter<SurveysView> {

    private final SurveyInteractor interactor = MicronesiaApplication.getInjection().getSurveyComponent().getSurveyInteractor();
    private final DataSource dataSource = MicronesiaApplication.getInjection().getDataSourceComponent().getDataRepository();
    private final LocalSettings localSettings = MicronesiaApplication.getInjection().getCoreComponent().getLocalSettings();
    private final SettingsInteractor settingsInteractor = MicronesiaApplication.getInjection().getAppComponent().getSettingsInteractor();
    private final OfflineSyncUseCase offlineSyncUseCase = MicronesiaApplication.getInjection().getOfflineSyncComponent().getUseCase();
    private final RemoteStorageAccessor remoteStorageAccessor = MicronesiaApplication.getInjection().getRemoteStorageComponent().getRemoteStorageAccessor();
    private final RemoteStorage remoteStorage = MicronesiaApplication.getInjection().getRemoteStorageComponent().getRemoteStorage();
    private final FilesRepository filesRepository = MicronesiaApplication.getInjection().getCoreComponent().getFilesRepository();
    private final Context appContext = MicronesiaApplication.getInjection().getCoreComponent().getContext();
    private final LogsRepository logsRepository = MicronesiaApplication.getInjection().getDataSourceComponent().getLogsRepository();

    private List<Survey> surveys = new ArrayList<>();

    private Survey surveyToDelete;
    private Survey surveyToChangeDate;

    private boolean requestLoadPartiallySavedSurvey;

    public SurveysPresenter() {
        super(MicronesiaApplication.getInjection().getOfflineSyncComponent().getAccessor());
        checkDriveChanges();
        subscribeOnSurveyUploadState();

        switch (localSettings.getSurveyTypeOrDefault()) {
            case SCHOOL_ACCREDITATION:
                getViewState().setTitle(Text.from(R.string.title_school_accreditation));
                getViewState().setExportEnabled(localSettings.isExportToExcelEnabled());
                break;
            case WASH:
                getViewState().setTitle(Text.from(R.string.title_wash));
                getViewState().setExportEnabled(false);
                break;
            default:
                throw new NotImplementedException();
        }
    }

    @Override
    public void attachView(SurveysView view) {
        super.attachView(view);
        loadRecentSurveys();
    }

    private void loadRecentSurveys() {
        addDisposable(interactor.getAllSurveys(localSettings.getCurrentAppRegion())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(surveys -> {
                    this.surveys = surveys;
                    getViewState().setSurveys(new ArrayList<>(this.surveys));
                }, this::handleError));
    }

    public void onSurveyPressed(Survey survey) {
        interactor.setCurrentSurvey(survey);
        getViewState().navigateToSurvey();
    }

    public void onSurveyMergePressed(Survey survey) {
        offlineSyncUseCase.executeAsInitiator(survey);
    }

    public void onSurveyExportToExcelPressed(Survey survey) {
        if (survey instanceof AccreditationSurvey) {
            addDisposable(
                    remoteStorageAccessor.exportToExcel((AccreditationSurvey) survey, ExportType.PRIVATE)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnSubscribe(d -> getViewState().showWaiting())
                            .doFinally(getViewState()::hideWaiting)
                            .subscribe(url -> {
                                if (!url.isEmpty()) {
                                    getViewState().openInExternalApp(Uri.parse(url));
                                }
                            }, this::handleError)
            );
        }
    }

    public void onSurveyRemovePressed(Survey survey) {
        surveyToDelete = survey;
        getViewState().promptMasterPassword(Text.from(R.string.message_delete_password_prompt));
    }

    public void onSurveyChangeDatePressed(Survey survey) {
        surveyToChangeDate = survey;
        getViewState().promptMasterPassword(Text.from(R.string.message_change_survey_date_prompt));
    }

    public void onExportAllPressed() {
        addDisposable(
                interactor.getAllSurveys(localSettings.getCurrentAppRegion())
                        .flatMapObservable(Observable::fromIterable)
                        .filter(s -> s.getState() == SurveyState.COMPLETED)
                        .cast(AccreditationSurvey.class)
                        .concatMapSingle(survey -> remoteStorageAccessor.exportToExcel(survey, ExportType.GLOBAL))
                        .toList()
                        .flatMap(urls -> {
                            if (urls.isEmpty()) {
                                return Single.just(Uri.EMPTY);
                            }
                            return RemoteStorageUtils.downloadReportFromUrl(appContext, remoteStorage, filesRepository, urls.get(0));
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> getViewState().showWaiting())
                        .doFinally(() -> getViewState().hideWaiting())
                        .subscribe(uri -> {
                            if (uri.equals(Uri.EMPTY)) {
                                getViewState().showMessage(Text.from(R.string.title_info), Text.from(R.string.message_nothing_to_export));
                            } else {
                                getViewState().openInExternalApp(uri);
//                                getViewState().showMessage(Text.from(R.string.title_info), Text.from(R.string.format_exported_to, url));
                            }
                        }, this::handleError)
        );
    }

    public void onLoadPartiallySavedSurveyPressed() {
        requestLoadPartiallySavedSurvey = true;
        getViewState().promptMasterPassword(Text.from(R.string.message_load_partially_password_prompt));
    }

    @NonNull
    @Override
    protected String provideMasterPassword() {
        return localSettings.getMasterPassword();
    }

    @NonNull
    @Override
    protected String provideFactoryPassword() {
        return localSettings.getFactoryPassword();
    }

    @Override
    protected void onMasterPasswordValidated() {
        if (requestLoadPartiallySavedSurvey) {
            requestLoadPartiallySavedSurvey = false;
            addDisposable(settingsInteractor.createFilledSurveyFromCloud()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(d -> getViewState().showWaiting())
                    .doFinally(() -> getViewState().hideWaiting())
                    .subscribe(this::loadRecentSurveys, this::handleError));
        } else if (surveyToDelete != null) {
            deleteSurvey();
        } else if (surveyToChangeDate != null) {
            changeDateSurvey();
        }
    }

    private void subscribeOnSurveyUploadState() {
        addDisposable(
                remoteStorage.getUploadStateObservable()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::updateUploadState, this::handleError));
    }

    private void updateUploadState(Pair<Long, UploadState> uploadStatePair) {
        surveys = surveys.stream()
                .map(survey -> {
                    if (survey.getId() == uploadStatePair.first) {
                        MutableSurvey mutableSurvey = survey.toMutable();
                        mutableSurvey.setUploadState(uploadStatePair.second);
                        return mutableSurvey;
                    } else {
                        return survey;
                    }
                })
                .collect(Collectors.toList());
        getViewState().setSurveys(new ArrayList<>(this.surveys));
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

    private void fetchNewPageToken() {
        addDisposable(
                remoteStorage.fetchStartPageToken()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(localSettings::setDrivePageToken, Throwable::printStackTrace));
    }

    private void deleteSurvey() {
        addDisposable(dataSource.deleteSurvey(surveyToDelete.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(() -> {
                    getViewState().removeSurvey(surveyToDelete);
                    surveyToDelete = null;
                }, this::handleError));
    }

    private void changeDateSurvey() {
        getViewState().showInputDialog(
                Text.from(R.string.title_change_date_survey),
                Text.from(surveyToChangeDate.getSurveyTag()),
                (date) -> {
                    surveyToChangeDate.toMutable().setSurveyTag(date);
                    dataSource.setSurveyUploadState(surveyToChangeDate, UploadState.NOT_UPLOAD);
                    loadRecentSurveys();
                    remoteStorageAccessor.scheduleUploading(surveyToChangeDate.getId());
                }
        );
    }

    private void deletedDriveSurveys(List<Survey> result) {
        if (!result.isEmpty()) {
            result.forEach(this::deleteSurvey);
        }
        fetchNewPageToken();
    }

    private void deleteSurvey(Survey survey) {
        addDisposable(logsRepository.deleteSurvey(survey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(() -> {
                    saveDeletedSurveyInfo(survey);
                    getViewState().removeSurvey(survey);
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
}
