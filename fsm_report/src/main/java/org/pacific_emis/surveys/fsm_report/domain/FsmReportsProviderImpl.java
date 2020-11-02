package org.pacific_emis.surveys.fsm_report.domain;

import java.util.Arrays;
import java.util.List;

import org.pacific_emis.surveys.accreditation_core.data.model.AccreditationSurvey;
import org.pacific_emis.surveys.accreditation_core.interactors.AccreditationSurveyInteractor;
import org.pacific_emis.surveys.fsm_report.levels.LevelsFragment;
import org.pacific_emis.surveys.report_core.domain.ReportInteractor;
import org.pacific_emis.surveys.report_core.model.ReportPage;
import org.pacific_emis.surveys.report_core.ui.recommendations.RecommendationsFragment;
import org.pacific_emis.surveys.report_core.ui.summary.SummaryFragment;

public class FsmReportsProviderImpl implements FsmReportsProvider {

    private final List<ReportPage> pages;
    private final ReportInteractor reportInteractor;
    private final AccreditationSurveyInteractor surveyInteractor;

    public FsmReportsProviderImpl(ReportInteractor reportInteractor, AccreditationSurveyInteractor surveyInteractor) {
        this.reportInteractor = reportInteractor;
        this.surveyInteractor = surveyInteractor;
        pages = Arrays.asList(
                new ReportPage(LevelsFragment.class, this.reportInteractor),
                new ReportPage(SummaryFragment.class, this.reportInteractor),
                new ReportPage(RecommendationsFragment.class, this.reportInteractor)
        );
    }

    @Override
    public void requestReports() {
        reportInteractor.requestReports((AccreditationSurvey) surveyInteractor.getCurrentSurvey());
    }

    @Override
    public List<ReportPage> getPages() {
        return pages;
    }

}
