package org.pacific_emis.surveys.rmi_report.domain;

import java.util.Arrays;
import java.util.List;

import org.pacific_emis.surveys.accreditation_core.data.model.AccreditationSurvey;
import org.pacific_emis.surveys.accreditation_core.interactors.AccreditationSurveyInteractor;
import org.pacific_emis.surveys.report_core.domain.ReportInteractor;
import org.pacific_emis.surveys.report_core.model.ReportPage;
import org.pacific_emis.surveys.report_core.ui.recommendations.RecommendationsFragment;
import org.pacific_emis.surveys.report_core.ui.summary.SummaryFragment;
import org.pacific_emis.surveys.rmi_report.ui.LevelsFragment;

public class RmiReportsProviderImpl implements RmiReportsProvider {

    private final List<ReportPage> pages;

    private final ReportInteractor reportInteractor;
    private final AccreditationSurveyInteractor accreditationSurveyInteractor;

    public RmiReportsProviderImpl(ReportInteractor reportInteractor, AccreditationSurveyInteractor accreditationSurveyInteractor) {
        this.reportInteractor = reportInteractor;
        this.accreditationSurveyInteractor = accreditationSurveyInteractor;
        pages = Arrays.asList(
                new ReportPage(LevelsFragment.class, reportInteractor),
                new ReportPage(SummaryFragment.class, reportInteractor),
                new ReportPage(RecommendationsFragment.class, reportInteractor)
        );
    }

    @Override
    public void requestReports() {
        reportInteractor.requestReports((AccreditationSurvey) accreditationSurveyInteractor.getCurrentSurvey());
    }

    @Override
    public List<ReportPage> getPages() {
        return pages;
    }
}
