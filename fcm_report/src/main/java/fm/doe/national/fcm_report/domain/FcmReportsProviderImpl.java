package fm.doe.national.fcm_report.domain;

import java.util.Arrays;
import java.util.List;

import fm.doe.national.core.data.model.Survey;
import fm.doe.national.fcm_report.ui.levels.LevelsFragment;
import fm.doe.national.report_core.domain.ReportInteractor;
import fm.doe.national.report_core.model.ReportPage;
import fm.doe.national.report_core.ui.recommendations.RecommendationsFragment;
import fm.doe.national.report_core.ui.summary.SummaryFragment;

public class FcmReportsProviderImpl implements FcmReportsProvider {

    private final List<ReportPage> pages;

    private final ReportInteractor interactor;

    public FcmReportsProviderImpl(ReportInteractor reportInteractor) {
        interactor = reportInteractor;
        pages = Arrays.asList(
                new ReportPage(LevelsFragment.class, interactor),
                new ReportPage(SummaryFragment.class, interactor),
                new ReportPage(RecommendationsFragment.class, interactor)
        );
    }

    @Override
    public void requestReports(Survey survey) {
        interactor.requestReports(survey);
    }

    @Override
    public List<ReportPage> getPages() {
        return pages;
    }

}
