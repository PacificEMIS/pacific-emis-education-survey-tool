package fm.doe.national.ui.screens.menu;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.omega_r.libs.omegatypes.Image;
import com.omega_r.libs.omegatypes.Text;
import com.omega_r.libs.omegatypes.UrlImageExtensionsKt;
import com.omegar.mvp.InjectViewState;

import fm.doe.national.R;
import fm.doe.national.app_support.MicronesiaApplication;
import fm.doe.national.core.preferences.GlobalPreferences;
import fm.doe.national.core.preferences.entities.SurveyType;
import fm.doe.national.offline_sync.domain.OfflineSyncUseCase;
import fm.doe.national.offline_sync.ui.base.BaseBluetoothPresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;

@InjectViewState
public class MainMenuPresenter extends BaseBluetoothPresenter<MainMenuView> {

    private final OfflineSyncUseCase offlineSyncUseCase = MicronesiaApplication.getInjection()
            .getOfflineSyncComponent()
            .getUseCase();

    private final GlobalPreferences globalPreferences = MicronesiaApplication.getInjection()
            .getCoreComponent()
            .getGlobalPreferences();

    public MainMenuPresenter() {
        super(MicronesiaApplication.getInjection().getOfflineSyncComponent().getAccessor());
        getViewState().setCurrentSurveyType(globalPreferences.getSurveyType());
    }

    @Override
    public void attachView(MainMenuView view) {
        super.attachView(view);

        String logoPath = globalPreferences.getLogoPath();

        if (logoPath != null) {
            getViewState().setIcon(UrlImageExtensionsKt.from(Image.Companion, logoPath));
        }

        String appName = globalPreferences.getAppName();
        getViewState().setTitle(TextUtils.isEmpty(appName) ? Text.from(R.string.app_name) : Text.from(appName));
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

    public void onMergePressed() {
        addDisposable(
                offlineSyncUseCase.executeAsReceiver()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getViewState()::showMergeProgress, this::handleError)
        );

    }
}
