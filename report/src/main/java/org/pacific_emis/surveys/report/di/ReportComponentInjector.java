package org.pacific_emis.surveys.report.di;

public class ReportComponentInjector {

    public static ReportComponent getComponent(Object injectionSource) {
        if (injectionSource instanceof ReportComponentProvider) {
            return ((ReportComponentProvider) injectionSource).provideReportComponent();
        } else {
            throw new IllegalStateException("Injection source is not a ReportComponentProvider");
        }
    }

}
