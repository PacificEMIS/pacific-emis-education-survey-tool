package org.pacific_emis.surveys.ui.screens.splash;

import com.omegar.mvp.InjectViewState;

import org.pacific_emis.surveys.app_support.MicronesiaApplication;
import org.pacific_emis.surveys.core.ui.screens.base.BasePresenter;
import org.pacific_emis.surveys.domain.SettingsInteractor;

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
