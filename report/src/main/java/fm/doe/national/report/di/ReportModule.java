package fm.doe.national.report.di;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.core.preferences.GlobalPreferences;
import fm.doe.national.fsm_report.di.FsmReportComponent;
import fm.doe.national.report_core.domain.ReportInteractor;
import fm.doe.national.report_core.domain.ReportsProvider;
import fm.doe.national.rmi_report.di.RmiReportComponent;

@Module
public class ReportModule {

    private final FsmReportComponent fsmReportComponent;
    private final RmiReportComponent rmiReportComponent;

    public ReportModule(FsmReportComponent fsmReportComponent, RmiReportComponent rmiReportComponent) {
        this.fsmReportComponent = fsmReportComponent;
        this.rmiReportComponent = rmiReportComponent;
    }

    @Provides
    public ReportsProvider provideReportsProvider(GlobalPreferences globalPreferences) {
        switch (globalPreferences.getAppRegion()) {
            case FSM:
                return fsmReportComponent.getFsmReportsProvider();
            case RMI:
                return rmiReportComponent.getRmiReportsProvider();
        }
        throw new IllegalStateException();
    }

    @Provides
    public ReportInteractor provideReportInteractor(GlobalPreferences globalPreferences) {
        switch (globalPreferences.getAppRegion()) {
            case FSM:
                return fsmReportComponent.getFsmReportInteractor();
            case RMI:
                return rmiReportComponent.getRmiReportInteractor();
        }
        throw new IllegalStateException();
    }
}
