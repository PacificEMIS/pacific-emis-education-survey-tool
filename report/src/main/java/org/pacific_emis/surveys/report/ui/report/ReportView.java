package org.pacific_emis.surveys.report.ui.report;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import org.pacific_emis.surveys.core.data.model.Teacher;
import org.pacific_emis.surveys.core.ui.screens.base.BaseView;
import org.pacific_emis.surveys.report_core.model.ReportPage;
import org.pacific_emis.surveys.report_core.ui.level_legend.LevelLegendView;

public interface ReportView extends BaseView {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void setReportPages(List<ReportPage> pages);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void setHeaderItem(LevelLegendView.Item item);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setPrincipalToAutocompleteField(List<Teacher> teachers);

}
