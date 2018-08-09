package fm.doe.national.ui.screens.main.slide_menu;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import fm.doe.national.ui.screens.base.BaseView;

/**
 * Created by Alexander Chibirev on 8/9/2018.
 */

@StateStrategyType(AddToEndSingleStrategy.class)
public interface SideMenuView extends BaseView {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showSchoolAccreditationScreen();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showSchoolDataVerificationScreen();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void shoMonitoringAndEvaluationScreen();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showEducationSurveyToolScreen();

    void hideMenu();

    void selectMenuOption(MenuItems menuOption);

    enum MenuItems {
        NONE,
        SCHOOL_ACCREDITATION,
        SCHOOL_DATA_VERIFICATION,
        MONITORING_AND_EVALUATION,
        EDUCATION_SURVEY_TOOL
    }

}
