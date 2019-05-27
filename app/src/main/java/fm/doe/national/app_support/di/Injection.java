package fm.doe.national.app_support.di;

import android.content.Context;

import fm.doe.national.core.di.CoreComponent;
import fm.doe.national.core.di.DaggerCoreComponent;
import fm.doe.national.core.di.modules.ContextModule;
import fm.doe.national.fcm_report.di.DaggerFcmReportComponent;
import fm.doe.national.fcm_report.di.FcmReportComponent;
import fm.doe.national.report.di.DaggerReportComponent;
import fm.doe.national.report.di.ReportComponent;
import fm.doe.national.report.di.ReportModule;
import fm.doe.national.rmi_report.di.DaggerRmiReportComponent;
import fm.doe.national.rmi_report.di.RmiReportComponent;

public class Injection {

    private CoreComponent coreComponent;
    private AppComponent appComponent;
    private FcmReportComponent fcmReportComponent;
    private RmiReportComponent rmiReportComponent;
    private ReportComponent reportComponent;

    public void createDependencyGraph(Context applicationContext) {
        coreComponent = DaggerCoreComponent
                .builder()
                .contextModule(new ContextModule(applicationContext))
                .build();
        appComponent = DaggerAppComponent.builder()
                .coreComponent(coreComponent)
                .build();
        fcmReportComponent = DaggerFcmReportComponent.builder().build();
        rmiReportComponent = DaggerRmiReportComponent.builder().build();
        reportComponent = DaggerReportComponent.builder()
                .coreComponent(coreComponent)
                .reportModule(new ReportModule(fcmReportComponent, rmiReportComponent))
                .build();
    }

    public CoreComponent getCoreComponent() {
        return coreComponent;
    }

    public FcmReportComponent getFcmReportComponent() {
        return fcmReportComponent;
    }

    public RmiReportComponent getRmiReportComponent() {
        return rmiReportComponent;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public ReportComponent getReportComponent() {
        return reportComponent;
    }
}
