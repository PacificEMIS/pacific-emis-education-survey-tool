package fm.doe.national.fcm_report.di;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.accreditation_core.di.AccreditationCoreComponent;
import fm.doe.national.fcm_report.domain.FsmReportInteractor;
import fm.doe.national.fcm_report.domain.FsmReportInteractorImpl;
import fm.doe.national.fcm_report.domain.FsmReportsProvider;
import fm.doe.national.fcm_report.domain.FsmReportsProviderImpl;

@Module
public class FsmReportModule {

    private final AccreditationCoreComponent accreditationCoreComponent;

    public FsmReportModule(AccreditationCoreComponent accreditationCoreComponent) {
        this.accreditationCoreComponent = accreditationCoreComponent;
    }

    @Provides
    @FsmReportScope
    FsmReportInteractor provideFsmReportInteractor() {
        return new FsmReportInteractorImpl();
    }

    @Provides
    @FsmReportScope
    FsmReportsProvider provideFsmReportsProvider(FsmReportInteractor reportInteractor) {
        return new FsmReportsProviderImpl(reportInteractor,accreditationCoreComponent.getAccreditationSurveyInteractor());
    }

}
