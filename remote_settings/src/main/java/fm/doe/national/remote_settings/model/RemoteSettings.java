package fm.doe.national.remote_settings.model;

import android.util.Log;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.gson.Gson;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import fm.doe.national.core.preferences.LocalSettings;
import fm.doe.national.core.utils.VoidArgFunction;
import fm.doe.national.core.utils.VoidFunction;
import fm.doe.national.remote_settings.BuildConfig;
import fm.doe.national.remote_storage.data.storage.RemoteStorage;

public class RemoteSettings {
    private static final String TAG = RemoteSettings.class.getName();
    private static final long TIMEOUT_FETCH_SEC = 5;
    private static final String KEY_MASTER_PASSWORD = "master_password";
    private static final String KEY_PROD_CERT = "prod_cert";
    private static final String KEY_LOGO_URL = "logo_url";
    private static final String KEY_EXPORT_TO_EXCEL = "can_export_to_excel";
    private static final String KEY_APP_TITLE = "app_title";
    private static final String KEY_CONTACT = "contact";

    private final LocalSettings localSettings;
    private final RemoteStorage remoteStorage;
    private final Executor executor = Executors.newCachedThreadPool();
    private final Gson gson = new Gson();

    private FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

    public RemoteSettings(LocalSettings localSettings, RemoteStorage remoteStorage) {
        this.localSettings = localSettings;
        this.remoteStorage = remoteStorage;
    }

    public void onAppCreate() {
        FirebaseRemoteConfigSettings settings = new FirebaseRemoteConfigSettings.Builder()
                .setFetchTimeoutInSeconds(TIMEOUT_FETCH_SEC)
                .setMinimumFetchIntervalInSeconds(BuildConfig.INTERVAL_FETCH_SEC)
                .build();
        firebaseRemoteConfig.setConfigSettingsAsync(settings)
                .addOnSuccessListener(executor, v -> fetch())
                .addOnFailureListener(executor, e -> Log.e(TAG, "setConfigSettingsAsync failed", e));
    }

    public void fetch() {
        firebaseRemoteConfig.fetchAndActivate()
                .addOnSuccessListener(executor, areUpdated -> {
                    if (areUpdated) {
                        parseRemoteSettings();
                    } else {
                        Log.i(TAG, "Fetch from local storage");
                    }
                })
                .addOnFailureListener(executor, e -> Log.e(TAG, "Fetch failed", e));
    }

    private void parseRemoteSettings() {
        parseForceableString(KEY_MASTER_PASSWORD, localSettings::setMasterPassword, localSettings::getMasterPassword);
        parseString(KEY_PROD_CERT, cert -> {
            localSettings.setProdCert(cert);
            remoteStorage.refreshCredentials();
        });
        parseForceableString(KEY_LOGO_URL, localSettings::setLogoPath, localSettings::getLogoPath);
        parseForceableBoolean(KEY_EXPORT_TO_EXCEL, localSettings::setExportToExcelEnabled, localSettings::isExportToExcelEnabled);
        parseForceableString(KEY_APP_TITLE, localSettings::setAppName, localSettings::getAppName);
        parseForceableString(KEY_CONTACT, localSettings::setContactName, localSettings::getContactName);
    }

    private void parseForceableBoolean(String key, VoidFunction<Boolean> setFunction, VoidArgFunction<Object> getFunction) {
        String value = firebaseRemoteConfig.getString(key);
        if (value != null) {
            ForceableBoolean remoteValue = gson.fromJson(value, ForceableBoolean.class);
            if (remoteValue.isForce() || getFunction.apply() == null) {
                setFunction.apply(remoteValue.getValue());
            }
        }
    }

    private void parseForceableString(String key, VoidFunction<String> setFunction, VoidArgFunction<Object> getFunction) {
        String value = firebaseRemoteConfig.getString(key);
        if (value != null) {
            ForceableString remoteValue = gson.fromJson(value, ForceableString.class);
            if (remoteValue.isForce() || getFunction.apply() == null) {
                setFunction.apply(remoteValue.getValue());
            }
        }
    }

    private void parseString(String key, VoidFunction<String> function) {
        String value = firebaseRemoteConfig.getString(key);
        if (value != null) {
            function.apply(value);
        }
    }
}
