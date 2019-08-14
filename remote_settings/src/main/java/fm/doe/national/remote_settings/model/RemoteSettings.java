package fm.doe.national.remote_settings.model;

import android.util.Log;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import fm.doe.national.core.preferences.LocalSettings;
import fm.doe.national.remote_storage.data.storage.RemoteStorage;

public class RemoteSettings {
    private static final String TAG = RemoteSettings.class.getName();
    private static final long TIMEOUT_FETCH_SEC = 5;
    private static final long INTERVAL_FETCH_SEC = 12; // 12 hours
    private static final String KEY_MASTER_PASSWORD = "master_password";
    private static final String KEY_PROD_CERT = "prod_cert";

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
        String masterPassword = firebaseRemoteConfig.getString(KEY_MASTER_PASSWORD);
        if (masterPassword != null) {
            localSettings.setMasterPassword(masterPassword);
        }

        String prodCert = firebaseRemoteConfig.getString(KEY_PROD_CERT);
        if (prodCert != null) {
            localSettings.setProdCert(prodCert);
            remoteStorage.refreshCredentials();
        }
    }
}
