package fm.doe.national.rmi_report.ui;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import fm.doe.national.report_core.ui.base.BaseReportView;
import fm.doe.national.rmi_report.model.SchoolAccreditationTallyLevel;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface LevelsView extends BaseReportView {

    void setData(SchoolAccreditationTallyLevel data);

}
