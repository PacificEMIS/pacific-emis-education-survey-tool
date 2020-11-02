package org.pacific_emis.surveys.rmi_report.di;

public class RmiReportComponentInjector {

    public static RmiReportComponent getComponent(Object injectionSource) {
        if (injectionSource instanceof RmiReportComponentProvider) {
            return ((RmiReportComponentProvider) injectionSource).provideRmiReportComponent();
        } else {
            throw new IllegalStateException("Injection source is not a RmiReportComponentProvider");
        }
    }

}
