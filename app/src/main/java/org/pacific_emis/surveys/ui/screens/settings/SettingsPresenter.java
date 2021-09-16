package org.pacific_emis.surveys.ui.screens.settings;

import android.content.ContentResolver;
import android.net.Uri;

import androidx.annotation.Nullable;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.InjectViewState;

import org.pacific_emis.surveys.BuildConfig;
import org.pacific_emis.surveys.R;
import org.pacific_emis.surveys.app_support.MicronesiaApplication;
import org.pacific_emis.surveys.core.data.local_data_source.DataSource;
import org.pacific_emis.surveys.core.data.remote_data_source.CoreRemoteDataSource;
import org.pacific_emis.surveys.core.preferences.LocalSettings;
import org.pacific_emis.surveys.core.ui.screens.base.BasePresenter;
import org.pacific_emis.surveys.domain.SettingsInteractor;
import org.pacific_emis.surveys.remote_settings.model.RemoteSettings;
import org.pacific_emis.surveys.remote_storage.data.storage.RemoteStorage;
import org.pacific_emis.surveys.ui.screens.settings.items.Item;
import org.pacific_emis.surveys.ui.screens.settings.items.OptionsItemFactory;

import java.util.ArrayList;
import java.util.Arrays;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class SettingsPresenter extends BasePresenter<SettingsView> {

    private final static String MIME_TYPE_SCHOOLS = "text/csv";
    private final static String MIME_TYPE_CERTIFICATE = "application/octet-stream";

    private final SettingsInteractor interactor = MicronesiaApplication.getInjection().getAppComponent().getSettingsInteractor();
    private final LocalSettings localSettings = MicronesiaApplication.getInjection().getCoreComponent().getLocalSettings();
    private final RemoteSettings remoteSettings = MicronesiaApplication.getInjection()
            .getRemoteSettingsComponent()
            .getRemoteSettings();
    private final RemoteStorage remoteStorage = MicronesiaApplication.getInjection()
            .getRemoteStorageComponent()
            .getRemoteStorage();
    private final OptionsItemFactory itemFactory = new OptionsItemFactory();
    private final DataSource remoteDataSource = MicronesiaApplication.getInjection().getCoreComponent().getRemoteDataSource();

    @Nullable
    private ExternalDocumentPickerCallback documentPickerCallback;

    public SettingsPresenter() {
        refresh();
    }

    private void refresh() {
        ArrayList<Item> items = new ArrayList<>(Arrays.asList(
                itemFactory.createLogoItem(),
                itemFactory.createPasswordItem(),
                itemFactory.createContextItem(localSettings.getAppRegion().getName()),
                itemFactory.createExportToExcelItem(localSettings.isExportToExcelEnabled()),
                itemFactory.createNameItem(localSettings.getAppName()),
                itemFactory.createContactItem(Text.from(localSettings.getContactName())),
                itemFactory.createOpModeItem(localSettings.getOperatingMode().getName()),
                itemFactory.createImportSchoolsItem(),
                itemFactory.createLoadSchoolsItem(localSettings.getAppRegion().getName()),
                itemFactory.createLoadTeachersItem(localSettings.getAppRegion().getName()),
                itemFactory.createLoadSubjectsItem(localSettings.getAppRegion().getName()),
                itemFactory.createTemplatesItem(),
                itemFactory.createForceFetchRemoteSettingsItem(),
                itemFactory.createLoadProdCertificateItem(),
                itemFactory.createDebugStorageItem()
        ));

        if (BuildConfig.DEBUG) {
            items.add(itemFactory.createDebugBuildInfoItem());
        }

        getViewState().setItems(items);
    }

    public void onItemPressed(Item item) {
        switch (item.getType()) {
            case DEBUG_STORAGE:
                onDebugStoragePressed();
                break;
            case CONTACT:
                onContactPressed();
                break;
            case CONTEXT:
                onContextPressed();
                break;
            case EXPORT_FOLDER:
                onChooseFolderPressed();
                break;
            case IMPORT_SCHOOLS:
                onImportSchoolsPressed();
                break;
            case LOAD_SCHOOLS:
                onLoadSchoolsPressed();
                break;
            case LOAD_TEACHERS:
                onLoadTeachersPressed();
                break;
            case LOAD_SUBJECTS:
                onLoadSubjectsPressed();
                break;
            case LOGO:
                onLogoPressed();
                break;
            case OPERATING_MODE:
                onOperatingModePressed();
                break;
            case NAME:
                onNamePressed();
                break;
            case PASSWORD:
                onChangeMasterPasswordPressed();
                break;
            case TEMPLATES:
                onTemplatesPressed();
                break;
            case REMOTE_SETTIGNS:
                onForceFetchRemoteSettingsPressed();
                break;
            case LOAD_PROD_CERTIFICATE:
                onLoadProdCertificatePressed();
                break;
        }
    }

    private void onLoadProdCertificatePressed() {
        documentPickerCallback = (contentResolver, uri) -> {
            String cert = readExternalUriToString(contentResolver, uri);
            localSettings.setProdCert(cert);
            remoteStorage.refreshCredentials();
            getViewState().showToast(Text.from(R.string.toast_prod_cert_loaded));
        };
        getViewState().openExternalDocumentsPicker(MIME_TYPE_CERTIFICATE);
    }

    private void onForceFetchRemoteSettingsPressed() {
        addDisposable(
                remoteSettings.forceFetch()
                        .flatMapCompletable(b -> interactor.loadDataFromAssets())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(d -> getViewState().showWaiting())
                        .doFinally(getViewState()::hideWaiting)
                        .subscribe(()-> {
                                this.refresh();
                                remoteSettings.init(null);
                        }, this::handleError)
        );
    }

    public void onBooleanValueChanged(Item item, boolean value) {
        switch (item.getType()) {
            case EXPORT_TO_EXCEL:
                localSettings.setExportToExcelEnabled(value);
                break;
        }
    }

    private void onDebugStoragePressed() {
        interactor.showDebugStorage();
    }

    private void onLogoPressed() {
        getViewState().navigateToChangeLogo();
    }

    private void onContextPressed() {
        getViewState().showRegionSelector(region -> {
            localSettings.setAppRegion(region);
            refresh();
            getViewState().showPrompt(
                    Text.from(R.string.title_info),
                    Text.from(R.string.message_prompt_fetch_remote),
                    this::onForceFetchRemoteSettingsPressed
            );
        });
    }

    private void onNamePressed() {
        getViewState().showInputDialog(
                Text.from(R.string.title_input_name),
                localSettings.getAppName(),
                (name) -> {
                    localSettings.setAppName(name);
                    refresh();
                }
        );
    }

    private void onContactPressed() {
        getViewState().showInputDialog(
                Text.from(R.string.title_input_contact),
                Text.from(localSettings.getContactName()),
                (name) -> {
                    localSettings.setContactName(name);
                    refresh();
                }
        );
    }

    private void onOperatingModePressed() {
        getViewState().showOperatingModeSelector(mode -> {
            interactor.setOperatingMode(mode);
            refresh();
        });
    }

    private void onImportSchoolsPressed() {
        documentPickerCallback = (contentResolver, uri) -> {
            String content = readExternalUriToString(contentResolver, uri);
            if (content != null) {
                addDisposable(interactor.importSchools(content)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> getViewState().showWaiting())
                        .doFinally(() -> getViewState().hideWaiting())
                        .subscribe(
                                () -> getViewState().showToast(Text.from(R.string.toast_import_schools_success)),
                                this::handleError
                        ));
            }
        };
        getViewState().openExternalDocumentsPicker(MIME_TYPE_SCHOOLS);
    }

    private void onLoadSchoolsPressed() {
        addDisposable(interactor.updateSchoolsFromRemote()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(() -> getViewState().showToast(Text.from(R.string.toast_load_schools_success)), this::handleError));
    }

    private void onLoadTeachersPressed() {
        addDisposable(interactor.updateTeachersFromRemote()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(
                        () -> getViewState().showToast(Text.from(R.string.toast_load_teachers_success)),
                        ((SettingsPresenter) this)::handleError
                ));
    }

    private void onLoadSubjectsPressed() {
        addDisposable(interactor.updateSubjectsFromRemote()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(
                        () -> getViewState().showToast(Text.from(R.string.toast_load_subjects_success)),
                        ((SettingsPresenter) this)::handleError
                ));
    }

    @Override
    public void onExternalDocumentPicked(ContentResolver contentResolver, Uri uri) {
        if (documentPickerCallback != null) {
            documentPickerCallback.onExternalDocumentPicked(contentResolver, uri);
        }
    }

    private void onChooseFolderPressed() {
        addDisposable(interactor.selectExportFolder()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(this::refresh, this::handleError));
    }

    private void onTemplatesPressed() {
        getViewState().navigateToTemplates();
    }

    private void onChangeMasterPasswordPressed() {
        getViewState().navigateToChangePassword();
    }

}
