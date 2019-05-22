package fm.doe.national.app_support.di.modules;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.core.di.FeatureScope;
import fm.doe.national.domain.ReportInteractor;
import fm.doe.national.domain.ReportInteractorImpl;
import fm.doe.national.domain.SettingsInteractor;

@Module
public class InteractorsModule {
    @Provides
    public SettingsInteractor provideSettingsInteractor() {
        return new SettingsInteractor();
    }

    @Provides
    @FeatureScope
    public ReportInteractor provideReportInteractor() {
        return new ReportInteractorImpl();
    }
}
