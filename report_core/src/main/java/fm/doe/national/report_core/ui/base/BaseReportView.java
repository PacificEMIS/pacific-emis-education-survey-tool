package fm.doe.national.report_core.ui.base;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import fm.doe.national.core.ui.screens.base.BaseView;
import fm.doe.national.report_core.ui.level_legend.LevelLegendView;

public interface BaseReportView extends BaseView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setHeaderItem(LevelLegendView.Item item);

}
