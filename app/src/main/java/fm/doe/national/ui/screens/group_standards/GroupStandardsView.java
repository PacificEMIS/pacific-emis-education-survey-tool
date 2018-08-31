package fm.doe.national.ui.screens.group_standards;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import fm.doe.national.data.data_source.models.GroupStandard;
import fm.doe.national.data.data_source.models.Standard;
import fm.doe.national.ui.screens.base.BaseView;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface GroupStandardsView extends BaseView {

    void showGroupStandards(List<GroupStandard> groups);

    void showStandards(List<Standard> standards);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToStandardScreen(long passingId, long standardId);

    void setGlobalProgress(int completed, int total);

}
