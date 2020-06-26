package org.pacific_emis.surveys.fsm_report.di;

public class FsmReportComponentInjector {

    public static FsmReportComponent getComponent(Object injectionSource) {
        if (injectionSource instanceof FsmReportComponentProvider) {
            return ((FsmReportComponentProvider) injectionSource).provideFsmReportComponent();
        } else {
            throw new IllegalStateException("Injection source is not a FsmReportComponentProvider");
        }
    }

}
