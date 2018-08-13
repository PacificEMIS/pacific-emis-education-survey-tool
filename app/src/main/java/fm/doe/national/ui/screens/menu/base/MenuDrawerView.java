package fm.doe.national.ui.screens.menu.base;

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import fm.doe.national.ui.screens.base.BaseView;
import fm.doe.national.ui.screens.menu.drawer.BaseDrawerView;

/**
 * Created by Alexander Chibirev on 8/10/2018.
 */
public interface MenuDrawerView extends BaseDrawerView {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showSchoolAccreditationScreen();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showSchoolDataVerificationScreen();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void shoMonitoringAndEvaluationScreen();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showEducationSurveyToolScreen();

}
