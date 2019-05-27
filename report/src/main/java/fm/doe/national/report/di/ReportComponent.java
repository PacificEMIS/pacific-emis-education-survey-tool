package fm.doe.national.report.di;

import dagger.Component;
import fm.doe.national.core.di.CoreComponent;
import fm.doe.national.core.interactors.SurveyInteractor;
import fm.doe.national.report_core.domain.ReportsProvider;

@ReportScope
@Component(modules = {
        ReportModule.class
}, dependencies = {
        CoreComponent.class
})
public interface ReportComponent {

    ReportsProvider getReportsProvider();

    SurveyInteractor getSurveyInteractor();

}