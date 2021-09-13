package org.pacific_emis.surveys.app_support.di;

import android.content.Context;

import org.pacific_emis.surveys.accreditation_core.di.AccreditationCoreComponent;
import org.pacific_emis.surveys.accreditation_core.di.DaggerAccreditationCoreComponent;
import org.pacific_emis.surveys.app_support.di.modules.InteractorsModule;
import org.pacific_emis.surveys.core.di.CoreComponent;
import org.pacific_emis.surveys.core.di.DaggerCoreComponent;
import org.pacific_emis.surveys.core.di.modules.ContextModule;
import org.pacific_emis.surveys.core.di.modules.RemoteDataSourceModule;
import org.pacific_emis.surveys.data_source_injector.di.DaggerDataSourceComponent;
import org.pacific_emis.surveys.data_source_injector.di.DataSourceComponent;
import org.pacific_emis.surveys.data_source_injector.di.modules.DataSourceModule;
import org.pacific_emis.surveys.data_source_injector.di.modules.SerializersModule;
import org.pacific_emis.surveys.fsm_report.di.DaggerFsmReportComponent;
import org.pacific_emis.surveys.fsm_report.di.FsmReportComponent;
import org.pacific_emis.surveys.fsm_report.di.FsmReportModule;
import org.pacific_emis.surveys.offline_sync.di.DaggerOfflineSyncComponent;
import org.pacific_emis.surveys.offline_sync.di.OfflineSyncComponent;
import org.pacific_emis.surveys.offline_sync.di.OfflineSyncModule;
import org.pacific_emis.surveys.remote_settings.di.DaggerRemoteSettingsComponent;
import org.pacific_emis.surveys.remote_settings.di.RemoteSettingsComponent;
import org.pacific_emis.surveys.remote_settings.di.RemoteSettingsModule;
import org.pacific_emis.surveys.remote_storage.di.DaggerRemoteStorageComponent;
import org.pacific_emis.surveys.remote_storage.di.RemoteStorageComponent;
import org.pacific_emis.surveys.remote_storage.di.RemoteStorageModule;
import org.pacific_emis.surveys.report.di.DaggerReportComponent;
import org.pacific_emis.surveys.report.di.ReportComponent;
import org.pacific_emis.surveys.report.di.ReportModule;
import org.pacific_emis.surveys.rmi_report.di.DaggerRmiReportComponent;
import org.pacific_emis.surveys.rmi_report.di.RmiReportComponent;
import org.pacific_emis.surveys.rmi_report.di.RmiReportModule;
import org.pacific_emis.surveys.survey.di.DaggerSurveyComponent;
import org.pacific_emis.surveys.survey.di.SurveyComponent;
import org.pacific_emis.surveys.survey.di.modules.ProviderModule;
import org.pacific_emis.surveys.survey_core.di.DaggerSurveyCoreComponent;
import org.pacific_emis.surveys.survey_core.di.SurveyCoreComponent;
import org.pacific_emis.surveys.wash_core.di.DaggerWashCoreComponent;
import org.pacific_emis.surveys.wash_core.di.WashCoreComponent;

public class Injection {

    private CoreComponent coreComponent;
    private AppComponent appComponent;
    private FsmReportComponent fsmReportComponent;
    private RmiReportComponent rmiReportComponent;
    private ReportComponent reportComponent;
    private SurveyComponent surveyComponent;
    private SurveyCoreComponent surveyCoreComponent;
    private RemoteStorageComponent remoteStorageComponent;
    private AccreditationCoreComponent accreditationCoreComponent;
    private DataSourceComponent dataSourceComponent;
    private WashCoreComponent washCoreComponent;
    private OfflineSyncComponent offlineSyncComponent;
    private RemoteSettingsComponent remoteSettingsComponent;

    public void createDependencyGraph(Context applicationContext) {
        coreComponent = DaggerCoreComponent
                .builder()
                .contextModule(new ContextModule(applicationContext))
                .build();
        surveyCoreComponent = DaggerSurveyCoreComponent.builder()
                .coreComponent(coreComponent)
                .build();
        accreditationCoreComponent = DaggerAccreditationCoreComponent.builder()
                .coreComponent(coreComponent)
                .build();
        washCoreComponent = DaggerWashCoreComponent.builder()
                .coreComponent(coreComponent)
                .build();
        dataSourceComponent = DaggerDataSourceComponent.builder()
                .coreComponent(coreComponent)
                .dataSourceModule(new DataSourceModule(coreComponent, accreditationCoreComponent, washCoreComponent))
                .serializersModule(new SerializersModule(accreditationCoreComponent, washCoreComponent))
                .build();
        surveyComponent = DaggerSurveyComponent.builder()
                .providerModule(new ProviderModule(
                        surveyCoreComponent,
                        coreComponent,
                        accreditationCoreComponent,
                        washCoreComponent
                ))
                .build();
        remoteStorageComponent = DaggerRemoteStorageComponent.builder()
                .coreComponent(coreComponent)
                .remoteStorageModule(new RemoteStorageModule(dataSourceComponent))
                .build();
        appComponent = DaggerAppComponent.builder()
                .coreComponent(coreComponent)
                .interactorsModule(new InteractorsModule(
                        remoteStorageComponent,
                        dataSourceComponent,
                        applicationContext.getAssets(),
                        accreditationCoreComponent,
                        washCoreComponent))
                .build();
        fsmReportComponent = DaggerFsmReportComponent.builder()
                .fsmReportModule(new FsmReportModule(accreditationCoreComponent))
                .build();
        rmiReportComponent = DaggerRmiReportComponent.builder()
                .rmiReportModule(new RmiReportModule(accreditationCoreComponent))
                .build();
        reportComponent = DaggerReportComponent.builder()
                .coreComponent(coreComponent)
                .reportModule(new ReportModule(fsmReportComponent, rmiReportComponent))
                .build();
        offlineSyncComponent = DaggerOfflineSyncComponent.builder()
                .coreComponent(coreComponent)
                .offlineSyncModule(new OfflineSyncModule(washCoreComponent, accreditationCoreComponent))
                .build();
        remoteSettingsComponent = DaggerRemoteSettingsComponent.builder()
                .coreComponent(coreComponent)
                .remoteSettingsModule(new RemoteSettingsModule(remoteStorageComponent))
                .build();
    }

    public CoreComponent getCoreComponent() {
        return coreComponent;
    }

    public FsmReportComponent getFsmReportComponent() {
        return fsmReportComponent;
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

    public SurveyComponent getSurveyComponent() {
        return surveyComponent;
    }

    public SurveyCoreComponent getSurveyCoreComponent() {
        return surveyCoreComponent;
    }

    public AccreditationCoreComponent getAccreditationCoreComponent() {
        return accreditationCoreComponent;
    }

    public DataSourceComponent getDataSourceComponent() {
        return dataSourceComponent;
    }

    public WashCoreComponent getWashCoreComponent() {
        return washCoreComponent;
    }

    public RemoteStorageComponent getRemoteStorageComponent() {
        return remoteStorageComponent;
    }

    public OfflineSyncComponent getOfflineSyncComponent() {
        return offlineSyncComponent;
    }

    public RemoteSettingsComponent getRemoteSettingsComponent() {
        return remoteSettingsComponent;
    }
}
