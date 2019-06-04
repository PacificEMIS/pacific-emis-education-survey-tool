package fm.doe.national.report_core.domain;

import java.util.List;

import fm.doe.national.accreditation_core.data.model.AccreditationSurvey;
import fm.doe.national.report_core.model.SummaryViewData;
import fm.doe.national.report_core.model.recommendations.Recommendation;
import fm.doe.national.report_core.ui.level_legend.LevelLegendView;
import io.reactivex.subjects.Subject;


public interface ReportInteractor {

    void requestReports(AccreditationSurvey survey);

    Subject<List<Recommendation>> getRecommendationsSubject();

    Subject<List<SummaryViewData>> getSummarySubject();

    Subject<LevelLegendView.Item> getHeaderItemSubject();

}
