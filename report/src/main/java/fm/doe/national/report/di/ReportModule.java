package fm.doe.national.report.di;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.core.preferences.GlobalPreferences;
import fm.doe.national.fcm_report.di.FcmReportComponent;
import fm.doe.national.report_core.domain.ReportInteractor;
import fm.doe.national.report_core.domain.ReportsProvider;
import fm.doe.national.rmi_report.di.RmiReportComponent;

@Module
public class ReportModule {

    private final FcmReportComponent fcmReportComponent;
    private final RmiReportComponent rmiReportComponent;

    public ReportModule(FcmReportComponent fcmReportComponent, RmiReportComponent rmiReportComponent) {
        this.fcmReportComponent = fcmReportComponent;
        this.rmiReportComponent = rmiReportComponent;
    }

    @Provides
    public ReportsProvider provideReportsProvider(GlobalPreferences globalPreferences) {
        switch (globalPreferences.getAppRegion()) {
            case FCM:
                return fcmReportComponent.getFcmReportsProvider();
            case RMI:
                return rmiReportComponent.getRmiReportsProvider();
        }
        throw new IllegalStateException();
    }

    @Provides
    public ReportInteractor provideReportInteractor(GlobalPreferences globalPreferences) {
        switch (globalPreferences.getAppRegion()) {
            case FCM:
                return fcmReportComponent.getFcmReportInteractor();
            case RMI:
                return rmiReportComponent.getRmiReportInteractor();
        }
        throw new IllegalStateException();
    }
}
