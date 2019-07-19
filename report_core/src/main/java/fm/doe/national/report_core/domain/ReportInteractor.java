package fm.doe.national.report_core.domain;

import java.util.List;

import fm.doe.national.accreditation_core.data.model.AccreditationSurvey;
import fm.doe.national.report_core.model.SummaryViewData;
import fm.doe.national.report_core.model.recommendations.FlattenRecommendationsWrapper;
import fm.doe.national.report_core.model.recommendations.Recommendation;
import fm.doe.national.report_core.ui.level_legend.LevelLegendView;
import io.reactivex.Observable;
import io.reactivex.Single;


public interface ReportInteractor {

    void requestReports(AccreditationSurvey survey);

    Observable<List<Recommendation>> getRecommendationsObservable();

    Observable<List<SummaryViewData>> getSummarySubjectObservable();

    Observable<LevelLegendView.Item> getHeaderItemObservable();

    Single<List<SummaryViewData>> requestFlattenSummary(AccreditationSurvey survey);

    Single<FlattenRecommendationsWrapper> requestFlattenRecommendations(AccreditationSurvey survey);

}
