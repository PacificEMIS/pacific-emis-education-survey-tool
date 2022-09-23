package org.pacific_emis.surveys.report_core.domain;

import java.util.List;

import org.pacific_emis.surveys.core.data.model.Survey;
import org.pacific_emis.surveys.report_core.model.ReportPage;

public interface ReportsProvider {

    void requestReports();

    Survey getCurrentSurvey();

    List<ReportPage> getPages();

}
