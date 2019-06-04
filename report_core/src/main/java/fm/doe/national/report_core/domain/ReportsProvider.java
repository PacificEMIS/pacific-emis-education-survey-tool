package fm.doe.national.report_core.domain;

import java.util.List;

import fm.doe.national.report_core.model.ReportPage;

public interface ReportsProvider {

    void requestReports();

    List<ReportPage> getPages();

}
