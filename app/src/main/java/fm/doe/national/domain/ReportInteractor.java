package fm.doe.national.domain;

import java.util.List;

import fm.doe.national.data.model.Survey;
import fm.doe.national.data.model.recommendations.Recommendation;
import fm.doe.national.ui.screens.report.levels.SchoolAccreditationLevel;
import fm.doe.national.ui.screens.report.summary.SummaryViewData;
import io.reactivex.subjects.Subject;

public interface ReportInteractor {

    void requestReports(Survey survey);

    Subject<List<Recommendation>> getRecommendationsSubject();

    Subject<SchoolAccreditationLevel> getLevelSubject();

    Subject<List<SummaryViewData>> getSummarySubject();

}
