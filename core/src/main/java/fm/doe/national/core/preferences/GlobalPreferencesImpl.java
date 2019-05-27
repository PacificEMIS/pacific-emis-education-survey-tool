package fm.doe.national.core.preferences;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import fm.doe.national.core.preferences.entities.AppRegion;

public class GlobalPreferencesImpl implements GlobalPreferences {

    private static final String PREF_KEY_APP_CONTEXT = "PREF_KEY_APP_CONTEXT";
    private static final AppRegion DEFAULT_APP_CONTEXT = AppRegion.FCM;
    private static final int NULL_APP_CONTEXT_VALUE = -1;
    private static final String PREF_KEY_LOGO_PATH = "PREF_KEY_LOGO_PATH";

    private final SharedPreferences sharedPreferences;

    public GlobalPreferencesImpl(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @NonNull
    @Override
    public AppRegion getAppContext() {
        AppRegion savedAppRegion = getSavedAppContext();
        return savedAppRegion != null ? savedAppRegion : DEFAULT_APP_CONTEXT;
    }

    @Nullable
    private AppRegion getSavedAppContext() {
        return AppRegion.createFromValue(sharedPreferences.getInt(PREF_KEY_APP_CONTEXT, NULL_APP_CONTEXT_VALUE));
    }


    @Override
    public void setAppContext(AppRegion appRegion) {
        sharedPreferences.edit().putInt(PREF_KEY_APP_CONTEXT, appRegion.getValue()).apply();
    }

    @Nullable
    @Override
    public String getLogoPath() {
        return sharedPreferences.getString(PREF_KEY_LOGO_PATH, null);
    }

    @Override
    public void setLogoPath(@Nullable String path) {
        sharedPreferences.edit().putString(PREF_KEY_LOGO_PATH, path).apply();
    }

    @Override
    public boolean isFirstLaunch() {
        return getSavedAppContext() == null;
    }
}
