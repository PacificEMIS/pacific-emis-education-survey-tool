package fm.doe.national.ui.screens.menu;

import androidx.annotation.NonNull;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.InjectViewState;

import fm.doe.national.R;
import fm.doe.national.app_support.MicronesiaApplication;
import fm.doe.national.core.preferences.GlobalPreferences;
import fm.doe.national.core.preferences.entities.SurveyType;
import fm.doe.national.core.ui.screens.base.BasePresenter;

@InjectViewState
public class MainMenuPresenter extends BasePresenter<MainMenuView> {

    private final GlobalPreferences globalPreferences = MicronesiaApplication.getInjection()
            .getCoreComponent()
            .getGlobalPreferences();

    public MainMenuPresenter() {
        getViewState().setCurrentSurveyType(globalPreferences.getSurveyType());
    }

    public void onCreditsPressed() {
        getViewState().navigateToCredits();
    }

    public void onSettingsPressed() {
        getViewState().promptMasterPassword(Text.from(R.string.message_settings_password_prompt));
    }

    @NonNull
    @Override
    protected String provideMasterPassword() {
        return globalPreferences.getMasterPassword();
    }

    @NonNull
    @Override
    protected String provideFactoryPassword() {
        return globalPreferences.getFactoryPassword();
    }

    @Override
    protected void onMasterPasswordValidated() {
        getViewState().navigateToSettings();
    }

    public void onAccreditationPressed() {
        globalPreferences.setSurveyType(SurveyType.SCHOOL_ACCREDITATION);
        getViewState().navigateToSurveys();
    }

    public void onWashPressed() {
        globalPreferences.setSurveyType(SurveyType.WASH);
        getViewState().navigateToSurveys();
    }

}
