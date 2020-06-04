package fm.doe.national.offline_sync.ui.surveys;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.ui.screens.base.BaseView;

public interface SyncSurveysView extends BaseView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setListLoadingVisible(boolean visible);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setSurveys(List<Survey> surveys);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void close();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToProgress();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setNextEnabled(boolean enabled);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setEmptyStateEnabled(boolean enabled);

}
