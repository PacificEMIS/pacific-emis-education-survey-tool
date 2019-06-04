package fm.doe.national.core.di.modules;

import android.content.Context;
import android.content.SharedPreferences;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.core.di.CoreScope;
import fm.doe.national.core.preferences.GlobalPreferences;
import fm.doe.national.core.preferences.GlobalPreferencesImpl;

import static android.content.Context.MODE_PRIVATE;

@Module
public class PreferencesModule {

    private static final String NAME_APP_PREFS_GLOBAL = "APP_PREFS_GLOBAL";

    @Provides
    @CoreScope
    public SharedPreferences provideSharedPreferences(Context context) {
        return context.getSharedPreferences(NAME_APP_PREFS_GLOBAL, MODE_PRIVATE);
    }

    @Provides
    @CoreScope
    public GlobalPreferences provideGlobalPreferences(SharedPreferences sp) {
        return new GlobalPreferencesImpl(sp);
    }

}