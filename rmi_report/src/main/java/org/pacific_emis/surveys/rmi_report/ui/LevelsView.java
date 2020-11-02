package org.pacific_emis.surveys.rmi_report.ui;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import org.pacific_emis.surveys.report_core.ui.base.BaseReportView;
import org.pacific_emis.surveys.rmi_report.model.SchoolAccreditationTallyLevel;

public interface LevelsView extends BaseReportView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setData(SchoolAccreditationTallyLevel data);

}
