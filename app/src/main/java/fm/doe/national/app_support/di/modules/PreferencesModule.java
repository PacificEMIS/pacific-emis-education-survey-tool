package fm.doe.national.app_support.di.modules;

import android.content.SharedPreferences;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.core.di.FeatureScope;
import fm.doe.national.data.preferences.GlobalPreferences;
import fm.doe.national.data.preferences.GlobalPreferencesImpl;

@Module
public class PreferencesModule {

    @Provides
    @FeatureScope
    public GlobalPreferences provideGlobalPreferences(SharedPreferences sp) {
        return new GlobalPreferencesImpl(sp);
    }

}