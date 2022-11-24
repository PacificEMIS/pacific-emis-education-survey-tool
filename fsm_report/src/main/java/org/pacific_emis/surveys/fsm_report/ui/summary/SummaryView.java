package org.pacific_emis.surveys.fsm_report.ui.summary;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import org.pacific_emis.surveys.report_core.ui.level_legend.LevelLegendView;
import org.pacific_emis.surveys.report_core.ui.summary.BaseSummaryView;

public interface SummaryView extends BaseSummaryView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void setHeaderItem(LevelLegendView.Item item);

}
