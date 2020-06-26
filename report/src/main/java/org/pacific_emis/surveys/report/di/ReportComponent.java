package org.pacific_emis.surveys.report.di;

import dagger.Component;
import org.pacific_emis.surveys.core.di.CoreComponent;
import org.pacific_emis.surveys.report_core.domain.ReportInteractor;
import org.pacific_emis.surveys.report_core.domain.ReportsProvider;

@ReportScope
@Component(modules = {
        ReportModule.class
}, dependencies = {
        CoreComponent.class
})
public interface ReportComponent {

    ReportsProvider getReportsProvider();

    ReportInteractor getReportInteractor();

}
