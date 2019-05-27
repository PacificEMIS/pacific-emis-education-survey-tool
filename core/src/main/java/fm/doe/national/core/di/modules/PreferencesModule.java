package fm.doe.national.core.di.modules;

import android.content.SharedPreferences;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.core.di.CoreScope;
import fm.doe.national.core.preferences.GlobalPreferences;
import fm.doe.national.core.preferences.GlobalPreferencesImpl;

@Module
public class PreferencesModule {

    @Provides
    @CoreScope
    public GlobalPreferences provideGlobalPreferences(SharedPreferences sp) {
        return new GlobalPreferencesImpl(sp);
    }

}