package fm.doe.national.ui.screens.settings;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.InjectViewState;

import java.util.ArrayList;
import java.util.Arrays;

import fm.doe.national.BuildConfig;
import fm.doe.national.R;
import fm.doe.national.app_support.MicronesiaApplication;
import fm.doe.national.core.preferences.GlobalPreferences;
import fm.doe.national.core.ui.screens.base.BasePresenter;
import fm.doe.national.domain.SettingsInteractor;
import fm.doe.national.ui.screens.settings.items.Item;
import fm.doe.national.ui.screens.settings.items.OptionsItemFactory;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class SettingsPresenter extends BasePresenter<SettingsView> {

    private final SettingsInteractor interactor = MicronesiaApplication.getInjection().getAppComponent().getSettingsInteractor();
    private final GlobalPreferences globalPreferences = MicronesiaApplication.getInjection().getCoreComponent().getGlobalPreferences();
    private final OptionsItemFactory itemFactory = new OptionsItemFactory();

    public SettingsPresenter() {
        refresh();
    }

    private void refresh() {
        ArrayList<Item> items = new ArrayList<>(Arrays.asList(
                itemFactory.createLogoItem(),
                itemFactory.createPasswordItem(),
                itemFactory.createContextItem(globalPreferences.getAppRegion().getName()),
                itemFactory.createNameItem(globalPreferences.getAppName()),
                itemFactory.createContactItem(Text.from(globalPreferences.getContactName())),
                itemFactory.createOpModeItem(globalPreferences.getOperatingMode().getName()),
                itemFactory.createImportSchoolsItem(),
                itemFactory.createTemplatesItem()
        ));

        if (BuildConfig.DEBUG) {
            items.add(itemFactory.createDebugStorageItem());
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
        }
    }

    private void onDebugStoragePressed() {
        interactor.showDebugStorage();
    }

    private void onLogoPressed() {
        getViewState().navigateToChangeLogo();
    }

    private void onContextPressed() {
        getViewState().showRegionSelector((region) -> {
            globalPreferences.setAppRegion(region);
            refresh();
        });
    }

    private void onNamePressed() {
        getViewState().showInputDialog(
                Text.from(R.string.title_input_name),
                globalPreferences.getAppName(),
                (name) -> {
                    globalPreferences.setAppName(name);
                    refresh();
                }
        );
    }

    private void onContactPressed() {
        getViewState().showInputDialog(
                Text.from(R.string.title_input_contact),
                Text.from(globalPreferences.getContactName()),
                (name) -> {
                    globalPreferences.setContactName(name);
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
        addDisposable(interactor.importSchools()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(() -> getViewState().showToast(Text.from(R.string.toast_import_schools_success)), this::handleError));
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
        getViewState().showToast(Text.from(R.string.coming_soon));
    }

    private void onChangeMasterPasswordPressed() {
        getViewState().navigateToChangePassword();
    }

}
