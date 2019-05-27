package fm.doe.national.fcm_report.di;

import dagger.Component;
import fm.doe.national.fcm_report.domain.FcmReportInteractor;
import fm.doe.national.fcm_report.domain.FcmReportsProvider;

@FcmReportScope
@Component(modules = {
        FcmReportModule.class
})
public interface FcmReportComponent {

    FcmReportInteractor getFcmReportInteractor();

    FcmReportsProvider getFcmReportsProvider();

}
