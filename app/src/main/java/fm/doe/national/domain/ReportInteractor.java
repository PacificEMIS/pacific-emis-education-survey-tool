package fm.doe.national.domain;

import java.util.List;

import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.data.model.recommendations.Recommendation;
import fm.doe.national.ui.custom_views.summary_header.SummaryHeaderView;
import fm.doe.national.ui.screens.report.levels.SchoolAccreditationLevel;
import fm.doe.national.ui.screens.report.summary.SummaryViewData;
import io.reactivex.subjects.Subject;

public interface ReportInteractor {

    void requestReports(Survey survey);

    Subject<List<Recommendation>> getRecommendationsSubject();

    Subject<SchoolAccreditationLevel> getLevelSubject();

    Subject<List<SummaryViewData>> getSummarySubject();

    Subject<SummaryHeaderView.Item> getHeaderItemSubject();

}
