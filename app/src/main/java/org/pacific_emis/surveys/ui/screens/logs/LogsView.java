package org.pacific_emis.surveys.ui.screens.logs;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import org.pacific_emis.surveys.core.data.model.SurveyLog;
import org.pacific_emis.surveys.core.ui.screens.base.BaseView;

import java.util.List;

public interface LogsView extends BaseView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setLogs(List<SurveyLog> logs);

}
