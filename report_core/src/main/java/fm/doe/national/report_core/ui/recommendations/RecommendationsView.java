package fm.doe.national.report_core.ui.recommendations;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import fm.doe.national.core.ui.screens.base.BaseView;
import fm.doe.national.report_core.model.recommendations.Recommendation;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface RecommendationsView extends BaseView {

    void setRecommendationsLoadingVisibility(boolean visible);

    void setRecommendations(List<Recommendation> recommendations);

}