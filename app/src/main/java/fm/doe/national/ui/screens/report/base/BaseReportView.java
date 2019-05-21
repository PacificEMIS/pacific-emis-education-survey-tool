package fm.doe.national.ui.screens.report.base;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import fm.doe.national.ui.custom_views.summary_header.SummaryHeaderView;
import fm.doe.national.ui.screens.base.BaseView;

public interface BaseReportView extends BaseView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setHeaderItem(SummaryHeaderView.Item item);

}
