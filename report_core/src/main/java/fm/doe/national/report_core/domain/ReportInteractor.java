package fm.doe.national.report_core.domain;

import java.util.List;

import fm.doe.national.core.data.model.Survey;
import fm.doe.national.report_core.model.SummaryViewData;
import fm.doe.national.report_core.model.recommendations.Recommendation;
import fm.doe.national.report_core.ui.summary_header.SummaryHeaderView;
import io.reactivex.subjects.Subject;


public interface ReportInteractor {

    void requestReports(Survey survey);

    Subject<List<Recommendation>> getRecommendationsSubject();

    Subject<List<SummaryViewData>> getSummarySubject();

    Subject<SummaryHeaderView.Item> getHeaderItemSubject();

}