package org.pacific_emis.surveys.ui.screens.password;

import com.omegar.mvp.InjectViewState;

import org.pacific_emis.surveys.app_support.MicronesiaApplication;
import org.pacific_emis.surveys.core.BuildConfig;
import org.pacific_emis.surveys.core.ui.screens.base.BasePresenter;
import org.pacific_emis.surveys.domain.SettingsInteractor;

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
