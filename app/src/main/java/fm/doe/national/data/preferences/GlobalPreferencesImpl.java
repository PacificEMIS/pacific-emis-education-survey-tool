package fm.doe.national.data.preferences;

import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import fm.doe.national.data.preferences.entities.AppContext;

public class GlobalPreferencesImpl implements GlobalPreferences {

    private static final String PREF_KEY_APP_CONTEXT = "PREF_KEY_APP_CONTEXT";
    private static final AppContext DEFAULT_APP_CONTEXT = AppContext.FCM;
    private static final int NULL_APP_CONTEXT_VALUE = -1;
    private static final String PREF_KEY_LOGO_PATH = "PREF_KEY_LOGO_PATH";

    private final SharedPreferences sharedPreferences;

    public GlobalPreferencesImpl(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @NotNull
    @Override
    public AppContext getAppContext() {
        AppContext savedAppContext = getSavedAppContext();
        return savedAppContext != null ? savedAppContext : DEFAULT_APP_CONTEXT;
    }

    @Nullable
    private AppContext getSavedAppContext() {
        return AppContext.createFromValue(sharedPreferences.getInt(PREF_KEY_APP_CONTEXT, NULL_APP_CONTEXT_VALUE));
    }


    @Override
    public void setAppContext(AppContext appContext) {
        sharedPreferences.edit().putInt(PREF_KEY_APP_CONTEXT, appContext.getValue()).apply();
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
