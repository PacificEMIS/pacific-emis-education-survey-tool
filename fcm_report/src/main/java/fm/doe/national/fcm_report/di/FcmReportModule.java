package fm.doe.national.fcm_report.di;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.fcm_report.domain.FcmReportInteractor;
import fm.doe.national.fcm_report.domain.FcmReportInteractorImpl;
import fm.doe.national.fcm_report.domain.FcmReportsProvider;
import fm.doe.national.fcm_report.domain.FcmReportsProviderImpl;

@Module
class FcmReportModule {

    @Provides
    @FcmReportScope
    FcmReportInteractor provideFcmReportInteractor() {
        return new FcmReportInteractorImpl();
    }

    @Provides
    @FcmReportScope
    FcmReportsProvider provideFcmReportsProvider(FcmReportInteractor interactor) {
        return new FcmReportsProviderImpl(interactor);
    }

}
