package fm.doe.national.ui.screens.surveys;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.ui.screens.base.BaseView;

@StateStrategyType(AddToEndSingleStrategy.class)
interface SurveysView extends BaseView {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToSurvey();

    void setSurveys(List<Survey> accreditations);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showSurveyDeleteConfirmation();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void removeSurvey(Survey passing);
}
