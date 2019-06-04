package fm.doe.national.fcm_report.di;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.accreditation_core.di.AccreditationCoreComponent;
import fm.doe.national.fcm_report.domain.FcmReportInteractor;
import fm.doe.national.fcm_report.domain.FcmReportInteractorImpl;
import fm.doe.national.fcm_report.domain.FcmReportsProvider;
import fm.doe.national.fcm_report.domain.FcmReportsProviderImpl;

@Module
public class FcmReportModule {

    private final AccreditationCoreComponent accreditationCoreComponent;

    public FcmReportModule(AccreditationCoreComponent accreditationCoreComponent) {
        this.accreditationCoreComponent = accreditationCoreComponent;
    }

    @Provides
    @FcmReportScope
    FcmReportInteractor provideFcmReportInteractor() {
        return new FcmReportInteractorImpl();
    }

    @Provides
    @FcmReportScope
    FcmReportsProvider provideFcmReportsProvider(FcmReportInteractor reportInteractor) {
        return new FcmReportsProviderImpl(reportInteractor,accreditationCoreComponent.getAccreditationSurveyInteractor());
    }

}
