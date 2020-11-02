package org.pacific_emis.surveys.rmi_report.di;

import dagger.Component;
import org.pacific_emis.surveys.rmi_report.domain.RmiReportInteractor;
import org.pacific_emis.surveys.rmi_report.domain.RmiReportsProvider;

@RmiReportScope
@Component(modules = {
        RmiReportModule.class
})
public interface RmiReportComponent {

    RmiReportInteractor getRmiReportInteractor();

    RmiReportsProvider getRmiReportsProvider();

}
