package org.pacific_emis.surveys.fsm_report.di;

import dagger.Module;
import dagger.Provides;
import org.pacific_emis.surveys.accreditation_core.di.AccreditationCoreComponent;
import org.pacific_emis.surveys.fsm_report.domain.FsmReportInteractor;
import org.pacific_emis.surveys.fsm_report.domain.FsmReportInteractorImpl;
import org.pacific_emis.surveys.fsm_report.domain.FsmReportsProvider;
import org.pacific_emis.surveys.fsm_report.domain.FsmReportsProviderImpl;

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
