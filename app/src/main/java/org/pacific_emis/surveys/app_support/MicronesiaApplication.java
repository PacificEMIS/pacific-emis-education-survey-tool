package org.pacific_emis.surveys.app_support;

import android.app.Application;

import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import com.omega_r.libs.omegatypes.image.GlideImagesProcessor;
import com.omegar.mvp.RegisterMoxyReflectorPackages;

import org.pacific_emis.surveys.R;
import org.pacific_emis.surveys.accreditation_core.di.AccreditationCoreComponent;
import org.pacific_emis.surveys.accreditation_core.di.AccreditationCoreComponentProvider;
import org.pacific_emis.surveys.app_support.di.Injection;
import org.pacific_emis.surveys.core.di.CoreComponent;
import org.pacific_emis.surveys.core.di.CoreComponentProvider;
import org.pacific_emis.surveys.data_source_injector.di.DataSourceComponent;
import org.pacific_emis.surveys.data_source_injector.di.DataSourceComponentProvider;
import org.pacific_emis.surveys.fsm_report.di.FsmReportComponent;
import org.pacific_emis.surveys.fsm_report.di.FsmReportComponentProvider;
import org.pacific_emis.surveys.offline_sync.di.OfflineSyncComponent;
import org.pacific_emis.surveys.offline_sync.di.OfflineSyncComponentProvider;
import org.pacific_emis.surveys.remote_settings.di.RemoteSettingsComponent;
import org.pacific_emis.surveys.remote_settings.di.RemoteSettingsComponentProvider;
import org.pacific_emis.surveys.remote_settings.model.RemoteSettings;
import org.pacific_emis.surveys.remote_storage.di.RemoteStorageComponent;
import org.pacific_emis.surveys.remote_storage.di.RemoteStorageComponentProvider;
import org.pacific_emis.surveys.report.di.ReportComponent;
import org.pacific_emis.surveys.report.di.ReportComponentProvider;
import org.pacific_emis.surveys.rmi_report.di.RmiReportComponent;
import org.pacific_emis.surveys.rmi_report.di.RmiReportComponentProvider;
import org.pacific_emis.surveys.survey.di.SurveyComponent;
import org.pacific_emis.surveys.survey.di.SurveyComponentProvider;
import org.pacific_emis.surveys.survey_core.di.SurveyCoreComponent;
import org.pacific_emis.surveys.survey_core.di.SurveyCoreComponentProvider;
import org.pacific_emis.surveys.wash_core.di.WashCoreComponent;
import org.pacific_emis.surveys.wash_core.di.WashCoreComponentProvider;

@RegisterMoxyReflectorPackages({
        "org.pacific_emis.surveys.fsm_report",
        "org.pacific_emis.surveys.rmi_report",
        "org.pacific_emis.surveys.report_core",
        "org.pacific_emis.surveys.report",
        "org.pacific_emis.surveys.accreditation",
        "org.pacific_emis.surveys.remote_storage",
        "org.pacific_emis.surveys.survey",
        "org.pacific_emis.surveys.wash",
        "org.pacific_emis.surveys.offline_sync",
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

        GlideImagesProcessor.Companion.setAsCurrentImagesProcessor();
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
