package fm.doe.national.report.ui.report;

import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;
import fm.doe.national.core.ui.screens.base.BaseView;
import fm.doe.national.report_core.model.ReportPage;
import fm.doe.national.report_core.ui.level_legend.LevelLegendView;

public interface ReportView extends BaseView {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void setReportPages(List<ReportPage> pages);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void setHeaderItem(LevelLegendView.Item item);

}
