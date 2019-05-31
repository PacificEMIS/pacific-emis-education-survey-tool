package fm.doe.national.fcm_report.di;

public class FcmReportComponentInjector {

    public static FcmReportComponent getComponent(Object injectionSource) {
        if (injectionSource instanceof FcmReportComponentProvider) {
            return ((FcmReportComponentProvider) injectionSource).provideFcmReportComponent();
        } else {
            throw new IllegalStateException("Injection source is not a FcmReportComponentProvider");
        }
    }

}
