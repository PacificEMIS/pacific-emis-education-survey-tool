package org.pacific_emis.surveys.rmi_report.di;

import dagger.Module;
import dagger.Provides;
import org.pacific_emis.surveys.accreditation_core.di.AccreditationCoreComponent;
import org.pacific_emis.surveys.rmi_report.domain.RmiReportInteractor;
import org.pacific_emis.surveys.rmi_report.domain.RmiReportInteractorImpl;
import org.pacific_emis.surveys.rmi_report.domain.RmiReportsProvider;
import org.pacific_emis.surveys.rmi_report.domain.RmiReportsProviderImpl;

@Module
public class RmiReportModule {

    private final AccreditationCoreComponent accreditationCoreComponent;

    public RmiReportModule(AccreditationCoreComponent accreditationCoreComponent) {
        this.accreditationCoreComponent = accreditationCoreComponent;
    }

    @Provides
    @RmiReportScope
    RmiReportInteractor provideRmiReportInteractor() {
        return new RmiReportInteractorImpl();
    }

    @Provides
    @RmiReportScope
    RmiReportsProvider provideRmiReportsProvider(RmiReportInteractor interactor) {
        return new RmiReportsProviderImpl(interactor, accreditationCoreComponent.getAccreditationSurveyInteractor());
    }
}
