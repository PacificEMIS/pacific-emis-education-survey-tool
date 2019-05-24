package fm.doe.national.report.di;

public class ComponentInjector {

    public static ReportComponent getComponent(Object injectionSource) {
        if (injectionSource instanceof ReportComponentProvider) {
            return ((ReportComponentProvider) injectionSource).provideReportComponent();
        } else {
            throw new IllegalStateException("Injection source is not a ReportComponentProvider");
        }
    }

}
