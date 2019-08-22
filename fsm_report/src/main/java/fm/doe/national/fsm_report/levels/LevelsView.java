package fm.doe.national.fsm_report.levels;


import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import fm.doe.national.fsm_report.model.SchoolAccreditationLevel;
import fm.doe.national.report_core.ui.base.BaseReportView;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface LevelsView extends BaseReportView {

    void setLoadingVisible(boolean visible);

    void setData(SchoolAccreditationLevel data);

}
