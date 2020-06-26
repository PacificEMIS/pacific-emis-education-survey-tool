package org.pacific_emis.surveys.fsm_report.di;

import dagger.Component;
import org.pacific_emis.surveys.fsm_report.domain.FsmReportInteractor;
import org.pacific_emis.surveys.fsm_report.domain.FsmReportsProvider;

@FsmReportScope
@Component(modules = {
        FsmReportModule.class
})
public interface FsmReportComponent {

    FsmReportInteractor getFsmReportInteractor();

    FsmReportsProvider getFsmReportsProvider();

}
