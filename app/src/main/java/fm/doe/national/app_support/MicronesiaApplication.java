package fm.doe.national.app_support;


import androidx.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.omegar.mvp.RegisterMoxyReflectorPackages;

import fm.doe.national.app_support.di.Injection;
import fm.doe.national.core.di.CoreComponent;
import fm.doe.national.core.di.CoreComponentProvider;
import fm.doe.national.fcm_report.di.FcmReportComponent;
import fm.doe.national.fcm_report.di.FcmReportComponentProvider;
import fm.doe.national.report.di.ReportComponent;
import fm.doe.national.report.di.ReportComponentProvider;
import io.fabric.sdk.android.Fabric;

@RegisterMoxyReflectorPackages({
        "fm.doe.national.fcm_report",
        "fm.doe.national.report_core",
        "fm.doe.national.report"
})
public class MicronesiaApplication extends MultiDexApplication implements
        CoreComponentProvider,
        FcmReportComponentProvider,
        ReportComponentProvider {

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
}
