package fm.doe.national.app_support;

import android.app.Application;

import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import com.omegar.mvp.RegisterMoxyReflectorPackages;

import fm.doe.national.R;
import fm.doe.national.accreditation_core.di.AccreditationCoreComponent;
import fm.doe.national.accreditation_core.di.AccreditationCoreComponentProvider;
import fm.doe.national.app_support.di.Injection;
import fm.doe.national.core.di.CoreComponent;
import fm.doe.national.core.di.CoreComponentProvider;
import fm.doe.national.data_source_injector.di.DataSourceComponent;
import fm.doe.national.data_source_injector.di.DataSourceComponentProvider;
import fm.doe.national.fsm_report.di.FsmReportComponent;
import fm.doe.national.fsm_report.di.FsmReportComponentProvider;
import fm.doe.national.offline_sync.di.OfflineSyncComponent;
import fm.doe.national.offline_sync.di.OfflineSyncComponentProvider;
import fm.doe.national.remote_settings.di.RemoteSettingsComponent;
import fm.doe.national.remote_settings.di.RemoteSettingsComponentProvider;
import fm.doe.national.remote_settings.model.RemoteSettings;
import fm.doe.national.remote_storage.di.RemoteStorageComponent;
import fm.doe.national.remote_storage.di.RemoteStorageComponentProvider;
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

@RegisterMoxyReflectorPackages({
        "fm.doe.national.fsm_report",
        "fm.doe.national.rmi_report",
        "fm.doe.national.report_core",
        "fm.doe.national.report",
        "fm.doe.national.accreditation",
        "fm.doe.national.remote_storage",
        "fm.doe.national.survey",
        "fm.doe.national.wash",
        "fm.doe.national.offline_sync",
})
public class MicronesiaApplication extends Application implements
        CoreComponentProvider,
        FsmReportComponentProvider,
        RmiReportComponentProvider,
        ReportComponentProvider,
        SurveyComponentProvider,
        SurveyCoreComponentProvider,
        RemoteStorageComponentProvider,
        AccreditationCoreComponentProvider,
        DataSourceComponentProvider,
        WashCoreComponentProvider,
        OfflineSyncComponentProvider,
        RemoteSettingsComponentProvider {

    private static final Injection injection = new Injection();

    @Override
    public void onCreate() {
        super.onCreate();
        AppCenter.start(this, getString(R.string.app_center_key), Analytics.class, Crashes.class);
        injection.createDependencyGraph(this);

        RemoteSettings remoteSettings = provideRemoteSettingsComponent().getRemoteSettings();
        remoteSettings.init(remoteSettings::fetchAsync);
    }

    public static Injection getInjection() {
        return injection;
    }

    @Override
    public CoreComponent provideCoreComponent() {
        return injection.getCoreComponent();
    }

    @Override
    public FsmReportComponent provideFsmReportComponent() {
        return injection.getFsmReportComponent();
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
    public RemoteStorageComponent provideRemoteStorageComponent() {
        return injection.getRemoteStorageComponent();
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

    @Override
    public OfflineSyncComponent provideOfflineSyncComponent() {
        return injection.getOfflineSyncComponent();
    }

    @Override
    public RemoteSettingsComponent provideRemoteSettingsComponent() {
        return injection.getRemoteSettingsComponent();
    }
}
