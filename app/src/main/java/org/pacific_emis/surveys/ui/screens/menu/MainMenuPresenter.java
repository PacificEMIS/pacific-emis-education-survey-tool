package org.pacific_emis.surveys.ui.screens.menu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.InjectViewState;

import org.pacific_emis.surveys.R;
import org.pacific_emis.surveys.app_support.MicronesiaApplication;
import org.pacific_emis.surveys.core.preferences.LocalSettings;
import org.pacific_emis.surveys.core.preferences.entities.SurveyType;
import org.pacific_emis.surveys.core.utils.UuidUtil;
import org.pacific_emis.surveys.offline_sync.domain.OfflineSyncUseCase;
import org.pacific_emis.surveys.offline_sync.ui.base.BaseBluetoothPresenter;
import org.pacific_emis.surveys.remote_storage.data.accessor.RemoteStorageAccessor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class MainMenuPresenter extends BaseBluetoothPresenter<MainMenuView> {

    private final OfflineSyncUseCase offlineSyncUseCase = MicronesiaApplication.getInjection()
            .getOfflineSyncComponent()
            .getUseCase();

    private final LocalSettings localSettings = MicronesiaApplication.getInjection()
            .getCoreComponent()
            .getLocalSettings();

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
        getViewState().setIcon(localSettings.getLogo());
        getViewState().setTitle(localSettings.getAppName());
    }

    public void onLicensePressed() {
        getViewState().navigateToLicense();
    }

    public void onSettingsPressed() {
        getViewState().promptMasterPassword(Text.from(R.string.message_settings_password_prompt));
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
        getViewState().navigateToSettings();
    }

    public void onAccreditationPressed() {
        localSettings.setSurveyType(SurveyType.SCHOOL_ACCREDITATION);
        getViewState().navigateToSurveys();
    }

    public void onWashPressed() {
        localSettings.setSurveyType(SurveyType.WASH);
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

    public void onLogsPressed() {
        getViewState().navigateToLogs();
    }

    private void updateUserData() {
        if (!localSettings.isTabletIdSaved()) localSettings.setTabletId(UuidUtil.generateUuid());
        if (!localSettings.isDrivePageTokenSaved()) {
            addDisposable(
                    remoteStorageAccessor.fetchStartPageToken()
                            .observeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(localSettings::setDrivePageToken)
            );
        }
        userEmail = remoteStorageAccessor.getUserEmail();
        getViewState().setAccountName(userEmail);
    }
}
