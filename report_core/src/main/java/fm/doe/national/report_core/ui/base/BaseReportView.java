package fm.doe.national.report_core.ui.base;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import fm.doe.national.core.ui.screens.base.BaseView;
import fm.doe.national.report_core.ui.summary_header.SummaryHeaderView;

public interface BaseReportView extends BaseView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setHeaderItem(SummaryHeaderView.Item item);

}
