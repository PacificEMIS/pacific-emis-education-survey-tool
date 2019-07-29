package fm.doe.national.fsm_report.di;

import dagger.Component;
import fm.doe.national.fsm_report.domain.FsmReportInteractor;
import fm.doe.national.fsm_report.domain.FsmReportsProvider;

@FsmReportScope
@Component(modules = {
        FsmReportModule.class
})
public interface FsmReportComponent {

    FsmReportInteractor getFsmReportInteractor();

    FsmReportsProvider getFsmReportsProvider();

}
