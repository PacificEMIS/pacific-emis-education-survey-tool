package fm.doe.national.report.ui.report;

import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import fm.doe.national.core.ui.screens.base.BaseView;
import fm.doe.national.report_core.model.ReportPage;

@StateStrategyType(OneExecutionStateStrategy.class)
public interface ReportView extends BaseView {

    void setReportPages(List<ReportPage> pages);

}
