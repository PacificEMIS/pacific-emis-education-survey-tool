package fm.doe.national.app_support;

import androidx.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.omegar.mvp.RegisterMoxyReflectorPackages;

import fm.doe.national.accreditation_core.di.AccreditationCoreComponent;
import fm.doe.national.accreditation_core.di.AccreditationCoreComponentProvider;
import fm.doe.national.app_support.di.Injection;
import fm.doe.national.cloud.di.CloudComponent;
import fm.doe.national.cloud.di.CloudComponentProvider;
import fm.doe.national.core.di.CoreComponent;
import fm.doe.national.core.di.CoreComponentProvider;
import fm.doe.national.data_source_injector.di.DataSourceComponent;
import fm.doe.national.data_source_injector.di.DataSourceComponentProvider;
import fm.doe.national.fcm_report.di.FcmReportComponent;
import fm.doe.national.fcm_report.di.FcmReportComponentProvider;
import fm.doe.national.report.di.ReportComponent;
import fm.doe.national.report.di.ReportComponentProvider;
import fm.doe.national.rmi_report.di.RmiReportComponent;
import fm.doe.national.rmi_report.di.RmiReportComponentProvider;
import fm.doe.national.survey.di.SurveyComponent;
import fm.doe.national.survey.di.SurveyComponentProvider;
import fm.doe.national.survey_core.di.SurveyCoreComponent;
import fm.doe.national.survey_core.di.SurveyCoreComponentProvider;
import fm.doe.national.wash_core.di.WashCoreComponent;
import fm.doe.national.wash_core.di.WashCoreComponentProvider;
import io.fabric.sdk.android.Fabric;

@RegisterMoxyReflectorPackages({
        "fm.doe.national.fcm_report",
        "fm.doe.national.rmi_report",
        "fm.doe.national.report_core",
        "fm.doe.national.report",
        "fm.doe.national.accreditation",
        "fm.doe.national.cloud",
        "fm.doe.national.survey_core",
        "fm.doe.national.survey",
        "fm.doe.national.wash"
})
public class MicronesiaApplication extends MultiDexApplication implements
        CoreComponentProvider,
        FcmReportComponentProvider,
        RmiReportComponentProvider,
        ReportComponentProvider,
        SurveyComponentProvider,
        SurveyCoreComponentProvider,
        CloudComponentProvider,
        AccreditationCoreComponentProvider,
        DataSourceComponentProvider,
        WashCoreComponentProvider {

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
    public CloudComponent provideCloudComponent() {
        return injection.getCloudComponent();
    }

    @Override
    public SurveyComponent provideSurveyComponent() {
        return injection.getSurveyComponent();
    }

    @Override
    public SurveyCoreComponent provideSurveyCoreComponent() {
        return injection.getSurveyCoreComponent();
    }

    @Override
    public AccreditationCoreComponent provideAccreditationCoreComponent() {
        return injection.getAccreditationCoreComponent();
    }

    @Override
    public DataSourceComponent provideDataSourceComponent() {
        return injection.getDataSourceComponent();
    }

    @Override
    public WashCoreComponent provideWashCoreComponent() {
        return injection.getWashCoreComponent();
    }
}
