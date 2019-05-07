package fm.doe.national.ui.screens.survey_creation;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.Date;
import java.util.List;

import fm.doe.national.data.model.School;
import fm.doe.national.ui.screens.base.BaseView;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface CreateSurveyView extends BaseView {

    void setSchools(List<School> schools);

    void setStartDate(Date date);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToCategoryChooser(long passingId);

    void showDatePicker(int currentYear, int currentMonth, int currentDay);
}
