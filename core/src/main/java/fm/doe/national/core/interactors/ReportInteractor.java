package fm.doe.national.core.interactors;

import java.util.List;

import fm.doe.national.core.data.model.SummaryViewData;
import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.data.model.recommendations.Recommendation;
import io.reactivex.subjects.Subject;

public interface ReportInteractor {

    void requestReports(Survey survey);

    Subject<List<Recommendation>> getRecommendationsSubject();

//    Subject<SchoolAccreditationLevel> getLevelSubject();

    Subject<List<SummaryViewData>> getSummarySubject();

//    Subject<SummaryHeaderView.Item> getHeaderItemSubject();

}
