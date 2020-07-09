package fm.doe.national.remote_settings.model;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import fm.doe.national.core.preferences.LocalSettings;
import fm.doe.national.core.preferences.entities.AppRegion;
import fm.doe.national.core.preferences.entities.OperatingMode;
import fm.doe.national.core.utils.VoidArgFunction;
import fm.doe.national.core.utils.VoidFunction;
import fm.doe.national.remote_settings.BuildConfig;
import fm.doe.national.remote_settings.R;
import fm.doe.national.remote_storage.data.storage.RemoteStorage;
import io.reactivex.Single;
import io.reactivex.subjects.SingleSubject;

public class RemoteSettings {
    private static final String TAG = RemoteSettings.class.getName();
    private static final long TIMEOUT_FETCH_SEC = 5;
    private static final String KEY_MASTER_PASSWORD = "master_password";
    private static final String KEY_LOGO_URL = "logo_url";
    private static final String KEY_EXPORT_TO_EXCEL = "can_export_to_excel";
    private static final String KEY_APP_TITLE = "app_title";
    private static final String KEY_CONTACT = "contact";
    private static final String KEY_PROD_CERT = "prod_cert";
    private static final String KEY_OPERATING_MODE = "operating_mode";

    private final LocalSettings localSettings;
    private final RemoteStorage remoteStorage;
    private final Executor executor = Executors.newCachedThreadPool();
    private final Gson gson = new Gson();

    private Map<AppRegion, FirebaseRemoteConfig> remoteConfigs = new HashMap<>();

    public RemoteSettings(Context appContext, LocalSettings localSettings, RemoteStorage remoteStorage) {
        this.localSettings = localSettings;
        this.remoteStorage = remoteStorage;
        configureFirebase(appContext);
    }

    private void configureFirebase(Context appContext) {
        remoteConfigs.put(
                AppRegion.FSM,
                FirebaseRemoteConfig.getInstance(FirebaseApp.initializeApp(
                        appContext,
                        new FirebaseOptions.Builder()
                                .setProjectId(appContext.getString(R.string.firebase_project_id_fsm))
                                .setApplicationId(appContext.getString(R.string.firebase_application_id_fsm))
                                .setApiKey(appContext.getString(R.string.firebase_api_key_fsm))
                                .build(),
                        Objects.requireNonNull(AppRegion.FSM.getName().getString(appContext))
                ))
        );
        remoteConfigs.put(
                AppRegion.RMI,
                FirebaseRemoteConfig.getInstance(FirebaseApp.initializeApp(
                        appContext,
                        new FirebaseOptions.Builder()
                                .setProjectId(appContext.getString(R.string.firebase_project_id_rmi))
                                .setApplicationId(appContext.getString(R.string.firebase_application_id_rmi))
                                .setApiKey(appContext.getString(R.string.firebase_api_key_rmi))
                                .build(),
                        Objects.requireNonNull(AppRegion.RMI.getName().getString(appContext))
                ))
        );
    }

    private FirebaseRemoteConfig getRemoteConfig() {
        return remoteConfigs.get(localSettings.getAppRegion());
    }

    public void init(@Nullable Runnable onSuccess) {
        init(BuildConfig.INTERVAL_FETCH_SEC, onSuccess);
    }

    private void init(long fetchInterval, @Nullable Runnable onSuccess) {
        FirebaseRemoteConfigSettings settings = new FirebaseRemoteConfigSettings.Builder()
                .setFetchTimeoutInSeconds(TIMEOUT_FETCH_SEC)
                .setMinimumFetchIntervalInSeconds(fetchInterval)
                .build();
        getRemoteConfig().setConfigSettingsAsync(settings)
                .addOnSuccessListener(executor, v -> {
                    if (onSuccess != null) {
                        onSuccess.run();
                    }
                })
                .addOnFailureListener(executor, e -> Log.e(TAG, "setConfigSettingsAsync failed", e));
    }

    public void fetchAsync() {
        fetch(null, false);
    }

    public Single<Boolean> forceFetch() {
        SingleSubject<Boolean> subject = SingleSubject.create();
        init(0, () -> fetch(subject, true));
        return subject;
    }

    private void fetch(@Nullable SingleSubject<Boolean> subject, boolean forcedByUser) {
        final VoidFunction<Boolean> notifyCompleted = isSuccess -> {
            if (subject != null) {
                subject.onSuccess(isSuccess);
            }
        };
        getRemoteConfig().fetchAndActivate()
                .addOnSuccessListener(executor, areUpdated -> {
                    if (areUpdated || forcedByUser) {
                        parseRemoteSettings(forcedByUser);
                        notifyCompleted.apply(true);
                    } else {
                        Log.i(TAG, "Fetch from local storage");
                        notifyCompleted.apply(false);
                    }
                })
                .addOnFailureListener(executor, e -> {
                    Log.e(TAG, "Fetch failed", e);
                    notifyCompleted.apply(false);
                });
    }

    private void parseRemoteSettings(boolean forcedByUser) {
        parseForceableString(
                KEY_MASTER_PASSWORD,
                forcedByUser,
                localSettings::setMasterPassword,
                localSettings::isMasterPasswordSaved
        );
        parseForceableString(KEY_LOGO_URL, forcedByUser, localSettings::setLogoPath, localSettings::isLogoSaved);
        parseForceableBoolean(
                KEY_EXPORT_TO_EXCEL,
                forcedByUser,
                localSettings::setExportToExcelEnabled,
                localSettings::isExportToExcelEnabled
        );
        parseForceableString(KEY_APP_TITLE, forcedByUser, localSettings::setAppName, localSettings::isAppNameSaved);
        parseForceableString(KEY_CONTACT, forcedByUser, localSettings::setContactName, localSettings::isContactNameSaved);
        parseForceableString(KEY_PROD_CERT, forcedByUser, cert64 -> {
            localSettings.setProdCert(decodeBase64(cert64));
            remoteStorage.refreshCredentials();
        }, () -> localSettings.getProdCert() == null);
        parseForceableString(KEY_OPERATING_MODE, forcedByUser, value -> {
            OperatingMode operatingMode = OperatingMode.createFromSerializedName(value);
            localSettings.setOperatingMode(operatingMode);
            remoteStorage.refreshCredentials();
        }, localSettings::isOperatingModeSaved);
    }

    private String decodeBase64(String decodedString) {
        return new String(Base64.decode(decodedString, Base64.DEFAULT));
    }

    private void parseForceableBoolean(String key,
                                       boolean forcedByUser,
                                       VoidFunction<Boolean> setFunction,
                                       VoidArgFunction<Boolean> existenceCheckFunction) {
        String value = getRemoteConfig().getString(key);
        if (value != null) {
            ForceableBoolean remoteValue = gson.fromJson(value, ForceableBoolean.class);

            if (remoteValue == null) {
                return;
            }

            if (forcedByUser || remoteValue.isForce() || !existenceCheckFunction.apply()) {
                setFunction.apply(remoteValue.getValue());
            }
        }
    }

    private void parseForceableString(String key,
                                      boolean forcedByUser,
                                      VoidFunction<String> setFunction,
                                      VoidArgFunction<Boolean> existenceCheckFunction) {
        String value = getRemoteConfig().getString(key);
        if (value != null) {
            ForceableString remoteValue = gson.fromJson(value, ForceableString.class);

            if (remoteValue == null) {
                return;
            }

            if (forcedByUser || remoteValue.isForce() || !existenceCheckFunction.apply()) {
                setFunction.apply(remoteValue.getValue());
            }
        }
    }
}
