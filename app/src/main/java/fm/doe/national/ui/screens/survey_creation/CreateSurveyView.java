package fm.doe.national.ui.screens.survey_creation;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import java.util.Date;
import java.util.List;

import fm.doe.national.core.data.model.School;
import fm.doe.national.ui.screens.base.BaseView;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface CreateSurveyView extends BaseView {

    void setSchools(List<School> schools);

    void setStartDate(Date date);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToCategoryChooser();

    void showDatePicker(int currentYear, int currentMonth, int currentDay);
}
