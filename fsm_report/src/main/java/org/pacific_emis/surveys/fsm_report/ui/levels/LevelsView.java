package org.pacific_emis.surveys.fsm_report.ui.levels;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import org.pacific_emis.surveys.fsm_report.model.SchoolAccreditationLevel;
import org.pacific_emis.surveys.report_core.ui.base.BaseReportView;

public interface LevelsView extends BaseReportView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setLoadingVisible(boolean visible);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setData(SchoolAccreditationLevel data);

}
