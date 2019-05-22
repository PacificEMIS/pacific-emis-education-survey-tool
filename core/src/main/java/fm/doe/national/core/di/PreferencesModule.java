package fm.doe.national.core.di;

import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.core.preferences.GlobalPreferences;
import fm.doe.national.core.preferences.GlobalPreferencesImpl;

@Module
public class PreferencesModule {

    @Provides
    @Singleton
    public GlobalPreferences provideGlobalPreferences(SharedPreferences sp) {
        return new GlobalPreferencesImpl(sp);
    }

}