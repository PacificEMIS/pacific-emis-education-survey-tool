package fm.doe.national.ui.screens.menu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.InjectViewState;

import fm.doe.national.R;
import fm.doe.national.app_support.MicronesiaApplication;
import fm.doe.national.core.preferences.GlobalPreferences;
import fm.doe.national.core.preferences.entities.SurveyType;
import fm.doe.national.offline_sync.domain.OfflineSyncUseCase;
import fm.doe.national.offline_sync.ui.base.BaseBluetoothPresenter;
import fm.doe.national.remote_storage.data.accessor.RemoteStorageAccessor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class MainMenuPresenter extends BaseBluetoothPresenter<MainMenuView> {

    private final OfflineSyncUseCase offlineSyncUseCase = MicronesiaApplication.getInjection()
            .getOfflineSyncComponent()
            .getUseCase();

    private final GlobalPreferences globalPreferences = MicronesiaApplication.getInjection()
            .getCoreComponent()
            .getGlobalPreferences();

    private final RemoteStorageAccessor remoteStorageAccessor = MicronesiaApplication.getInjection()
            .getRemoteStorageComponent()
            .getRemoteStorageAccessor();

    @Nullable
    private String userEmail;

    public MainMenuPresenter() {
        super(MicronesiaApplication.getInjection().getOfflineSyncComponent().getAccessor());
        updateUserData();
    }

    @Override
    public void attachView(MainMenuView view) {
        super.attachView(view);
        getViewState().setIcon(globalPreferences.getLogo());
        getViewState().setTitle(globalPreferences.getAppName());
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

    public void onAuthButtonPressed() {
        if (userEmail == null) {
            addDisposable(
                    remoteStorageAccessor.signInAsUser()
                            .observeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(this::updateUserData, t -> {}) // silence errors
            );
        } else {
            remoteStorageAccessor.signOutAsUser();
            updateUserData();
        }
    }

    private void updateUserData() {
        userEmail = remoteStorageAccessor.getUserEmail();
        getViewState().setAccountName(userEmail);
    }
}
