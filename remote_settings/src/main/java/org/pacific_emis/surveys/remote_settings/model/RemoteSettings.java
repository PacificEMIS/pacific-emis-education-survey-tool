package org.pacific_emis.surveys.remote_settings.model;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.gson.Gson;

import org.pacific_emis.surveys.core.preferences.LocalSettings;
import org.pacific_emis.surveys.core.preferences.entities.AppRegion;
import org.pacific_emis.surveys.core.preferences.entities.OperatingMode;
import org.pacific_emis.surveys.core.utils.VoidArgFunction;
import org.pacific_emis.surveys.core.utils.VoidFunction;
import org.pacific_emis.surveys.remote_settings.BuildConfig;
import org.pacific_emis.surveys.remote_settings.R;
import org.pacific_emis.surveys.remote_storage.data.storage.RemoteStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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
    private static final String KEY_EMIS_URL = "emis_url";
    private static final String KEY_EMIS_USER = "emis_user";
    private static final String KEY_EMIS_PASSWORD = "emis_password";

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
                                .setApplicationId(appContext.getString(R.string.firebase_application_id_fsm))
                                .setApiKey(appContext.getString(R.string.firebase_api_key_fsm))
                                .setProjectId(appContext.getString(R.string.firebase_project_id_fsm))
                                .build(),
                        Objects.requireNonNull(AppRegion.FSM.getName().getString(appContext))
                ))
        );
        remoteConfigs.put(
                AppRegion.RMI,
                FirebaseRemoteConfig.getInstance(FirebaseApp.initializeApp(
                        appContext,
                        new FirebaseOptions.Builder()
                                .setApplicationId(appContext.getString(R.string.firebase_application_id_rmi))
                                .setApiKey(appContext.getString(R.string.firebase_api_key_rmi))
                                .setProjectId(appContext.getString(R.string.firebase_project_id_rmi))
                                .build(),
                        Objects.requireNonNull(AppRegion.RMI.getName().getString(appContext))
                ))
        );
    }

    private FirebaseRemoteConfig getRemoteConfig() {
        return remoteConfigs.get(localSettings.getCurrentAppRegion());
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
        parseForceableString(KEY_EMIS_URL, forcedByUser, localSettings::setEmisApi, localSettings::isEmisApiSaved);
        parseForceableString(KEY_EMIS_USER, forcedByUser, localSettings::setEmisUser, localSettings::isEmisUserSaved);
        parseForceableString(KEY_EMIS_PASSWORD, forcedByUser, localSettings::setEmisPassword, localSettings::isEmisPasswordSaved);
    }

    private String decodeBase64(String decodedString) {
        return new String(Base64.decode(decodedString, Base64.DEFAULT));
    }

    private void parseForceableBoolean(String key,
                                       boolean forcedByUser,
                                       VoidFunction<Boolean> setFunction,
                                       VoidArgFunction<Boolean> existenceCheckFunction) {
        String value = getRemoteConfig().getString(key);
        if (!TextUtils.isEmpty(value)) {
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
        if (!TextUtils.isEmpty(value)) {
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
