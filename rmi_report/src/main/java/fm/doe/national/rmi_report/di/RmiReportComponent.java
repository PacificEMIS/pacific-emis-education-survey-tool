package fm.doe.national.rmi_report.di;

import dagger.Component;
import fm.doe.national.rmi_report.domain.RmiReportInteractor;
import fm.doe.national.rmi_report.domain.RmiReportsProvider;

@RmiReportScope
@Component(modules = {
        RmiReportModule.class
})
public interface RmiReportComponent {

    RmiReportInteractor getRmiReportInteractor();

    RmiReportsProvider getRmiReportsProvider();

}
