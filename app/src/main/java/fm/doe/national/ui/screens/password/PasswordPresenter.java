package fm.doe.national.ui.screens.password;

import com.omegar.mvp.InjectViewState;

import fm.doe.national.app_support.MicronesiaApplication;
import fm.doe.national.core.BuildConfig;
import fm.doe.national.core.ui.screens.base.BasePresenter;
import fm.doe.national.domain.SettingsInteractor;

@InjectViewState
public class PasswordPresenter extends BasePresenter<PasswordView> {

    private final SettingsInteractor settingsInteractor = MicronesiaApplication.getInjection().getAppComponent().getSettingsInteractor();

    private String newPassword = "";
    private String confirmPassword = "";

    public void onNewPasswordInput(String input) {
        newPassword = input;
        getViewState().setPasswordsNotMatchVisible(false);
        getViewState().setPasswordTooShortVisible(newPassword.length() < BuildConfig.MIN_PASSWORD_LENGTH);
    }

    public void onConfirmPasswordInput(String input) {
        confirmPassword = input;
        getViewState().setPasswordsNotMatchVisible(false);
    }

    public void onConfirmPressed() {
        if (newPassword.equals(confirmPassword)) {
            settingsInteractor.setMasterPassword(newPassword);
            getViewState().navigateToRegion();
        } else {
            getViewState().setPasswordsNotMatchVisible(true);
        }
    }
}
