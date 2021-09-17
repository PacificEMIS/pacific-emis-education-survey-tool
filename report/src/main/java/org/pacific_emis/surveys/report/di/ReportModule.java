package org.pacific_emis.surveys.report.di;

import dagger.Module;
import dagger.Provides;
import org.pacific_emis.surveys.core.preferences.LocalSettings;
import org.pacific_emis.surveys.fsm_report.di.FsmReportComponent;
import org.pacific_emis.surveys.report_core.domain.ReportInteractor;
import org.pacific_emis.surveys.report_core.domain.ReportsProvider;
import org.pacific_emis.surveys.rmi_report.di.RmiReportComponent;

@Module
public class ReportModule {

    private final FsmReportComponent fsmReportComponent;
    private final RmiReportComponent rmiReportComponent;

    public ReportModule(FsmReportComponent fsmReportComponent, RmiReportComponent rmiReportComponent) {
        this.fsmReportComponent = fsmReportComponent;
        this.rmiReportComponent = rmiReportComponent;
    }

    @Provides
    public ReportsProvider provideReportsProvider(LocalSettings localSettings) {
        switch (localSettings.getCurrentAppRegion()) {
            case FSM:
                return fsmReportComponent.getFsmReportsProvider();
            case RMI:
                return rmiReportComponent.getRmiReportsProvider();
        }
        throw new IllegalStateException();
    }

    @Provides
    public ReportInteractor provideReportInteractor(LocalSettings localSettings) {
        switch (localSettings.getCurrentAppRegion()) {
            case FSM:
                return fsmReportComponent.getFsmReportInteractor();
            case RMI:
                return rmiReportComponent.getRmiReportInteractor();
        }
        throw new IllegalStateException();
    }
}
