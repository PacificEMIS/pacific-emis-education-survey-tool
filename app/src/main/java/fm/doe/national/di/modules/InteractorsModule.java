package fm.doe.national.di.modules;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.domain.SettingsInteractor;
import fm.doe.national.domain.StandardInteractor;

@Module
public class InteractorsModule {
    @Provides
    public SettingsInteractor provideSettingsInteractor() {
        return new SettingsInteractor();
    }

    @Provides
    public StandardInteractor provideStandardInteractor() {
        return new StandardInteractor();
    }
}
