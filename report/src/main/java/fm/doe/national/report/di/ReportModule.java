package fm.doe.national.report.di;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.core.preferences.GlobalPreferences;
import fm.doe.national.fcm_report.di.FcmReportComponent;
import fm.doe.national.report_core.domain.ReportInteractor;
import fm.doe.national.report_core.domain.ReportsProvider;

@Module
public class ReportModule {

    private final FcmReportComponent fcmReportComponent;

    public ReportModule(FcmReportComponent fcmReportComponent) {
        this.fcmReportComponent = fcmReportComponent;
    }

    @Provides
    public ReportsProvider provideReportsProvider(GlobalPreferences globalPreferences) {
        switch (globalPreferences.getAppContext()) {
            case FCM:
                return fcmReportComponent.getFcmReportsProvider();
            case RMI:
                return fcmReportComponent.getFcmReportsProvider(); // TODO: not implemented
        }
        throw new IllegalStateException();
    }

    @Provides
    public ReportInteractor provideReportInteractor(GlobalPreferences globalPreferences) {
        switch (globalPreferences.getAppContext()) {
            case FCM:
                return fcmReportComponent.getFcmReportInteractor();
            case RMI:
                return fcmReportComponent.getFcmReportInteractor(); // TODO: not implemented
        }
        throw new IllegalStateException();
    }
}
