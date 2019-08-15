package fm.doe.national.remote_settings.model;

import android.util.Log;

import androidx.annotation.Nullable;

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

    private final LocalSettings localSettings;
    private final RemoteStorage remoteStorage;
    private final Executor executor = Executors.newCachedThreadPool();
    private final Gson gson = new Gson();

    private FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

    public RemoteSettings(LocalSettings localSettings, RemoteStorage remoteStorage) {
        this.localSettings = localSettings;
        this.remoteStorage = remoteStorage;
    }

    public void init(@Nullable Runnable onSuccess) {
        init(BuildConfig.INTERVAL_FETCH_SEC, onSuccess);
    }

    private void init(long fetchInterval, @Nullable Runnable onSuccess) {
        FirebaseRemoteConfigSettings settings = new FirebaseRemoteConfigSettings.Builder()
                .setFetchTimeoutInSeconds(TIMEOUT_FETCH_SEC)
                .setMinimumFetchIntervalInSeconds(fetchInterval)
                .build();
        firebaseRemoteConfig.setConfigSettingsAsync(settings)
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
        firebaseRemoteConfig.fetchAndActivate()
                .addOnSuccessListener(executor, areUpdated -> {
                    if (areUpdated) {
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
    }

    private void parseForceableBoolean(String key,
                                       boolean forcedByUser,
                                       VoidFunction<Boolean> setFunction, 
                                       VoidArgFunction<Boolean> existenceCheckFunction) {
        String value = firebaseRemoteConfig.getString(key);
        if (value != null) {
            ForceableBoolean remoteValue = gson.fromJson(value, ForceableBoolean.class);
            if (forcedByUser || remoteValue.isForce() || !existenceCheckFunction.apply()) {
                setFunction.apply(remoteValue.getValue());
            }
        }
    }

    private void parseForceableString(String key,
                                      boolean forcedByUser,
                                      VoidFunction<String> setFunction,
                                      VoidArgFunction<Boolean> existenceCheckFunction) {
        String value = firebaseRemoteConfig.getString(key);
        if (value != null) {
            ForceableString remoteValue = gson.fromJson(value, ForceableString.class);
            if (forcedByUser || remoteValue.isForce() || !existenceCheckFunction.apply()) {
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
