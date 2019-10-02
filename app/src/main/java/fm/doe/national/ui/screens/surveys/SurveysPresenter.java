package fm.doe.national.ui.screens.surveys;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.InjectViewState;

import java.util.ArrayList;
import java.util.List;

import fm.doe.national.R;
import fm.doe.national.accreditation_core.data.model.AccreditationSurvey;
import fm.doe.national.app_support.MicronesiaApplication;
import fm.doe.national.core.data.data_source.DataSource;
import fm.doe.national.core.data.exceptions.NotImplementedException;
import fm.doe.national.core.data.files.FilesRepository;
import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.data.model.SurveyState;
import fm.doe.national.core.domain.SurveyInteractor;
import fm.doe.national.core.preferences.LocalSettings;
import fm.doe.national.domain.SettingsInteractor;
import fm.doe.national.offline_sync.domain.OfflineSyncUseCase;
import fm.doe.national.offline_sync.ui.base.BaseBluetoothPresenter;
import fm.doe.national.remote_storage.data.accessor.RemoteStorageAccessor;
import fm.doe.national.remote_storage.data.model.ExportType;
import fm.doe.national.remote_storage.data.storage.RemoteStorage;
import fm.doe.national.remote_storage.utils.RemoteStorageUtils;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class SurveysPresenter extends BaseBluetoothPresenter<SurveysView> {

    private final SurveyInteractor interactor = MicronesiaApplication.getInjection().getSurveyComponent().getSurveyInteractor();
    private final DataSource dataSource = MicronesiaApplication.getInjection().getDataSourceComponent().getDataSource();
    private final LocalSettings localSettings = MicronesiaApplication.getInjection().getCoreComponent().getLocalSettings();
    private final SettingsInteractor settingsInteractor = MicronesiaApplication.getInjection().getAppComponent().getSettingsInteractor();
    private final OfflineSyncUseCase offlineSyncUseCase = MicronesiaApplication.getInjection().getOfflineSyncComponent().getUseCase();
    private final RemoteStorageAccessor remoteStorageAccessor = MicronesiaApplication.getInjection().getRemoteStorageComponent().getRemoteStorageAccessor();
    private final RemoteStorage remoteStorage = MicronesiaApplication.getInjection().getRemoteStorageComponent().getRemoteStorage();
    private final FilesRepository filesRepository = MicronesiaApplication.getInjection().getCoreComponent().getFilesRepository();
    private final Context appContext = MicronesiaApplication.getInjection().getCoreComponent().getContext();

    private List<Survey> surveys = new ArrayList<>();

    private Survey surveyToDelete;

    private boolean requestLoadPartiallySavedSurvey;

    public SurveysPresenter() {
        super(MicronesiaApplication.getInjection().getOfflineSyncComponent().getAccessor());

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
        addDisposable(interactor.getAllSurveys()
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

    public void onExportAllPressed() {
        addDisposable(
                interactor.getAllSurveys()
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
        }
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
}
