package fm.doe.national.remote_settings.model;

import android.util.Log;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import fm.doe.national.core.preferences.LocalSettings;
import fm.doe.national.core.utils.VoidFunction;
import fm.doe.national.remote_storage.data.storage.RemoteStorage;

public class RemoteSettings {
    private static final String TAG = RemoteSettings.class.getName();
    private static final long TIMEOUT_FETCH_SEC = 5;
    private static final long INTERVAL_FETCH_SEC = 12; // 12 hours
    private static final String KEY_MASTER_PASSWORD = "master_password";
    private static final String KEY_PROD_CERT = "prod_cert";
    private static final String KEY_LOGO_URL = "logo_url";
    private static final String KEY_EXPORT_TO_EXCEL = "can_export_to_excel";
    private static final String KEY_APP_TITLE = "app_title";
    private static final String KEY_CONTACT = "contact";

    private final LocalSettings localSettings;
    private final RemoteStorage remoteStorage;
    private final Executor executor = Executors.newCachedThreadPool();

    private FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

    public RemoteSettings(LocalSettings localSettings, RemoteStorage remoteStorage) {
        this.localSettings = localSettings;
        this.remoteStorage = remoteStorage;
    }

    public void onAppCreate() {
        FirebaseRemoteConfigSettings settings = new FirebaseRemoteConfigSettings.Builder()
                .setFetchTimeoutInSeconds(TIMEOUT_FETCH_SEC)
                .setMinimumFetchIntervalInSeconds(INTERVAL_FETCH_SEC)
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
                        Log.e(TAG, "Fetch from local storage");
                    }
                })
                .addOnFailureListener(executor, e -> Log.e(TAG, "Fetch failed", e));
    }

    private void parseRemoteSettings() {
        parseString(KEY_MASTER_PASSWORD, localSettings::setMasterPassword);
        parseString(KEY_PROD_CERT, cert -> {
            localSettings.setProdCert(cert);
            remoteStorage.refreshCredentials();
        });
        parseString(KEY_LOGO_URL, localSettings::setLogoPath);
        parseBoolean(KEY_EXPORT_TO_EXCEL, localSettings::setExportToExcelEnabled);
        parseString(KEY_APP_TITLE, localSettings::setAppName);
        parseString(KEY_CONTACT, localSettings::setContactName);
    }

    private void parseBoolean(String key, VoidFunction<Boolean> function) {
        boolean value = firebaseRemoteConfig.getBoolean(key);
        function.apply(value);
    }

    private void parseString(String key, VoidFunction<String> function) {
        String value = firebaseRemoteConfig.getString(key);
        if (value != null) {
            function.apply(value);
        }
    }
}
