package fm.doe.national.app_support.di.modules;

import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.data.preferences.GlobalPreferences;
import fm.doe.national.data.preferences.GlobalPreferencesImpl;

@Module
public class PreferencesModule {

    @Provides
    @Singleton
    public GlobalPreferences provideGlobalPreferences(SharedPreferences sp) {
        return new GlobalPreferencesImpl(sp);
    }

}