package fm.doe.national.ui.screens.splash;

import com.omegar.mvp.InjectViewState;

import fm.doe.national.app_support.MicronesiaApplication;
import fm.doe.national.core.ui.screens.base.BasePresenter;
import fm.doe.national.domain.SettingsInteractor;

@InjectViewState
public class SplashPresenter extends BasePresenter<SplashView> {

    private final SettingsInteractor interactor = MicronesiaApplication.getInjection().getAppComponent().getSettingsInteractor();

    SplashPresenter() {
        getViewState().requestAppPermissions();
    }

    public void onPermissionsGranted() {
        navigateToNextScreen();
    }

    private void navigateToNextScreen() {
        if (!interactor.isMasterPasswordSaved()) {
            getViewState().navigateToMasterPassword();
        } else if (interactor.getAppRegion() == null) {
            getViewState().navigateToRegionChoose();
        } else {
            getViewState().navigateToMenu();
        }
    }

}
