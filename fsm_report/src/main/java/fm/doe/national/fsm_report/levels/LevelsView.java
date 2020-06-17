package fm.doe.national.fsm_report.levels;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import fm.doe.national.fsm_report.model.SchoolAccreditationLevel;
import fm.doe.national.report_core.ui.base.BaseReportView;

public interface LevelsView extends BaseReportView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setLoadingVisible(boolean visible);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setData(SchoolAccreditationLevel data);

}
