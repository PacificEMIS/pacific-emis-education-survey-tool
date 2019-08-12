package fm.doe.national.core.remote_config;

import android.util.Log;

import com.google.firebase.BuildConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import fm.doe.national.core.preferences.GlobalPreferences;

public class RemoteConfig {
    private static final String TAG = RemoteConfig.class.getName();
    private static final long TIMEOUT_FETCH_SEC = 5;
    private static final long INTERVAL_FETCH_SEC = 12 * 60 * 60; // 12 hours
    private static final String KEY_MASTER_PASSWORD = "master_password";

    private final GlobalPreferences globalPreferences;
    private final Executor executor = Executors.newCachedThreadPool();

    private FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

    public RemoteConfig(GlobalPreferences globalPreferences) {
        this.globalPreferences = globalPreferences;
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
            globalPreferences.setMasterPassword(masterPassword);
        }
    }
}
