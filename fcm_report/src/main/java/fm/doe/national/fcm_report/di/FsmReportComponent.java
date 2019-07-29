package fm.doe.national.fcm_report.di;

import dagger.Component;
import fm.doe.national.fcm_report.domain.FsmReportInteractor;
import fm.doe.national.fcm_report.domain.FsmReportsProvider;

@FsmReportScope
@Component(modules = {
        FsmReportModule.class
})
public interface FsmReportComponent {

    FsmReportInteractor getFsmReportInteractor();

    FsmReportsProvider getFsmReportsProvider();

}
