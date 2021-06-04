package org.pacific_emis.surveys.ui.screens.survey_creation;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import java.util.Date;
import java.util.List;

import org.pacific_emis.surveys.core.data.model.School;
import org.pacific_emis.surveys.core.ui.screens.base.BaseView;

public interface CreateSurveyView extends BaseView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setSchools(List<School> schools);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setStartDate(Date date);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToSurvey();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showDatePicker(int currentYear, int currentMonth, int currentDay);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setContinueEnabled(boolean isEnabled);
}
