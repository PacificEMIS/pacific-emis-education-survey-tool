package fm.doe.national.di.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.domain.SettingsInteractor;

@Module
public class InteractorsModule {
    @Provides
    @Singleton
    public SettingsInteractor provideSettingsInteractor() {
        return new SettingsInteractor();
    }
}
