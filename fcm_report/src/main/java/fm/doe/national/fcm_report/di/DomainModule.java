package fm.doe.national.fcm_report.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.fcm_report.domain.FcmReportInteractor;
import fm.doe.national.fcm_report.domain.FcmReportInteractorImpl;

@Module
public class DomainModule {

    @Provides
    @Singleton
    public FcmReportInteractor provideFcmReportInteractor() {
        return new FcmReportInteractorImpl();
    }
}
