package fm.doe.national.app_support;


import androidx.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.omegar.mvp.RegisterMoxyReflectorPackages;

import fm.doe.national.accreditation.di.AccreditationComponent;
import fm.doe.national.accreditation.di.AccreditationComponentProvider;
import fm.doe.national.app_support.di.Injection;
import fm.doe.national.cloud.di.CloudComponent;
import fm.doe.national.cloud.di.CloudComponentProvider;
import fm.doe.national.core.di.CoreComponent;
import fm.doe.national.core.di.CoreComponentProvider;
import fm.doe.national.fcm_report.di.FcmReportComponent;
import fm.doe.national.fcm_report.di.FcmReportComponentProvider;
import fm.doe.national.report.di.ReportComponent;
import fm.doe.national.report.di.ReportComponentProvider;
import fm.doe.national.rmi_report.di.RmiReportComponent;
import fm.doe.national.rmi_report.di.RmiReportComponentProvider;
import io.fabric.sdk.android.Fabric;

@RegisterMoxyReflectorPackages({
        "fm.doe.national.fcm_report",
        "fm.doe.national.rmi_report",
        "fm.doe.national.report_core",
        "fm.doe.national.report",
        "fm.doe.national.accreditation",
        "fm.doe.national.cloud",
})
public class MicronesiaApplication extends MultiDexApplication implements
        CoreComponentProvider,
        FcmReportComponentProvider,
        RmiReportComponentProvider,
        ReportComponentProvider,
        AccreditationComponentProvider,
        CloudComponentProvider {

    private static final Injection injection = new Injection();

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        injection.createDependencyGraph(this);
    }

    public static Injection getInjection() {
        return injection;
    }

    @Override
    public CoreComponent provideCoreComponent() {
        return injection.getCoreComponent();
    }

    @Override
    public FcmReportComponent provideFcmReportComponent() {
        return injection.getFcmReportComponent();
    }

    @Override
    public ReportComponent provideReportComponent() {
        return injection.getReportComponent();
    }

    @Override
    public RmiReportComponent provideRmiReportComponent() {
        return injection.getRmiReportComponent();
    }

    @Override
    public AccreditationComponent provideAccreditationComponent() {
        return injection.getAccreditationComponent();
    }

    @Override
    public CloudComponent provideCloudComponent() {
        return injection.getCloudComponent();
    }
}
