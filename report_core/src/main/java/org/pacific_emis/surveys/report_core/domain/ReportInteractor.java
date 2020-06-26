package org.pacific_emis.surveys.report_core.domain;

import java.util.List;

import org.pacific_emis.surveys.accreditation_core.data.model.AccreditationSurvey;
import org.pacific_emis.surveys.report_core.model.SummaryViewData;
import org.pacific_emis.surveys.report_core.model.recommendations.FlattenRecommendationsWrapper;
import org.pacific_emis.surveys.report_core.model.recommendations.Recommendation;
import org.pacific_emis.surveys.report_core.ui.level_legend.LevelLegendView;
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
