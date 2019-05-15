package fm.doe.national.app_support.di.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.data.data_source.DataSource;
import fm.doe.national.domain.SettingsInteractor;
import fm.doe.national.domain.SurveyInteractor;
import fm.doe.national.domain.SurveyInteractorImpl;

@Module
public class InteractorsModule {
    @Provides
    public SettingsInteractor provideSettingsInteractor() {
        return new SettingsInteractor();
    }

    @Provides
    @Singleton
    public SurveyInteractor provideSurveyInteractor(DataSource dataSource) {
        return new SurveyInteractorImpl(dataSource);
    }
}
