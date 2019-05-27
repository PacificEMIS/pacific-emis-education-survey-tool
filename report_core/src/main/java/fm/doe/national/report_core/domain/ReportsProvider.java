package fm.doe.national.report_core.domain;

import java.util.List;

import fm.doe.national.report_core.model.ReportPage;
import fm.doe.national.core.data.model.Survey;

public interface ReportsProvider {

    void requestReports(Survey survey);

    List<ReportPage> getPages();

}
