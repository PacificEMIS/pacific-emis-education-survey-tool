package fm.doe.national.di.modules;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.domain.SettingsInteractor;

@Module
public class InteractorsModule {
    @Provides
    public SettingsInteractor provideSettingsInteractor() {
        return new SettingsInteractor();
    }
}
