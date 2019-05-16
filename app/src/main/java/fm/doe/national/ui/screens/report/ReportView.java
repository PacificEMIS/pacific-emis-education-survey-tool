package fm.doe.national.ui.screens.report;

import androidx.annotation.NonNull;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import fm.doe.national.ui.screens.base.BaseView;
import fm.doe.national.ui.screens.report.recommendations.Recommendation;
import fm.doe.national.ui.screens.report.summary.SummaryViewData;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface ReportView extends BaseView {

    void setSummaryLoadingVisibility(boolean visible);

    void setSummaryData(@NonNull List<SummaryViewData> data);

    void setRecommendationsLoadingVisibility(boolean visible);

    void setRecommendations(List<Recommendation> recommendations);

}
