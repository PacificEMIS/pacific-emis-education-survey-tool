package fm.doe.national.rmi_report.di;

public class ComponentInjector {

    public static RmiReportComponent getComponent(Object injectionSource) {
        if (injectionSource instanceof RmiReportComponentProvider) {
            return ((RmiReportComponentProvider) injectionSource).provideRmiReportComponent();
        } else {
            throw new IllegalStateException("Injection source is not a RmiReportComponentProvider");
        }
    }

}
