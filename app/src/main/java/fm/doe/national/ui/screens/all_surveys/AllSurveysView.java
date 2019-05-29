package fm.doe.national.ui.screens.all_surveys;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import fm.doe.national.core.data.model.Survey;
import fm.doe.national.ui.screens.menu.drawer.BaseDrawerView;

@StateStrategyType(AddToEndSingleStrategy.class)
interface AllSurveysView extends BaseDrawerView {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToSurvey();

    void setSurveys(List<Survey> accreditations);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showSurveyDeleteConfirmation();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void removeSurvey(Survey passing);
}
