package fm.doe.national.app_support.di;

import android.content.Context;

import fm.doe.national.accreditation_core.di.AccreditationCoreComponent;
import fm.doe.national.accreditation_core.di.DaggerAccreditationCoreComponent;
import fm.doe.national.app_support.di.modules.InteractorsModule;
import fm.doe.national.cloud.di.CloudComponent;
import fm.doe.national.cloud.di.DaggerCloudComponent;
import fm.doe.national.core.di.CoreComponent;
import fm.doe.national.core.di.DaggerCoreComponent;
import fm.doe.national.core.di.modules.ContextModule;
import fm.doe.national.data_source_injector.di.DaggerDataSourceComponent;
import fm.doe.national.data_source_injector.di.DataSourceComponent;
import fm.doe.national.data_source_injector.di.modules.DataSourceModule;
import fm.doe.national.data_source_injector.di.modules.SerializersModule;
import fm.doe.national.fcm_report.di.DaggerFcmReportComponent;
import fm.doe.national.fcm_report.di.FcmReportComponent;
import fm.doe.national.fcm_report.di.FcmReportModule;
import fm.doe.national.report.di.DaggerReportComponent;
import fm.doe.national.report.di.ReportComponent;
import fm.doe.national.report.di.ReportModule;
import fm.doe.national.rmi_report.di.DaggerRmiReportComponent;
import fm.doe.national.rmi_report.di.RmiReportComponent;
import fm.doe.national.rmi_report.di.RmiReportModule;
import fm.doe.national.survey.di.DaggerSurveyComponent;
import fm.doe.national.survey.di.SurveyComponent;
import fm.doe.national.survey.di.modules.ProviderModule;
import fm.doe.national.survey_core.di.DaggerSurveyCoreComponent;
import fm.doe.national.survey_core.di.SurveyCoreComponent;
import fm.doe.national.wash_core.di.DaggerWashCoreComponent;
import fm.doe.national.wash_core.di.WashCoreComponent;

public class Injection {

    private CoreComponent coreComponent;
    private AppComponent appComponent;
    private FcmReportComponent fcmReportComponent;
    private RmiReportComponent rmiReportComponent;
    private ReportComponent reportComponent;
    private SurveyComponent surveyComponent;
    private SurveyCoreComponent surveyCoreComponent;
    private CloudComponent cloudComponent;
    private AccreditationCoreComponent accreditationCoreComponent;
    private DataSourceComponent dataSourceComponent;
    private WashCoreComponent washCoreComponent;

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
                .dataSourceModule(new DataSourceModule(accreditationCoreComponent, washCoreComponent))
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
        cloudComponent = DaggerCloudComponent.builder()
                .coreComponent(coreComponent)
                .build();
        appComponent = DaggerAppComponent.builder()
                .coreComponent(coreComponent)
                .interactorsModule(new InteractorsModule(cloudComponent, applicationContext.getAssets(), dataSourceComponent))
                .build();
        fcmReportComponent = DaggerFcmReportComponent.builder()
                .fcmReportModule(new FcmReportModule(accreditationCoreComponent))
                .build();
        rmiReportComponent = DaggerRmiReportComponent.builder()
                .rmiReportModule(new RmiReportModule(accreditationCoreComponent))
                .build();
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

    public CloudComponent getCloudComponent() {
        return cloudComponent;
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
}
