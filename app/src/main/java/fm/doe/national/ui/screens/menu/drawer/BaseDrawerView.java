package fm.doe.national.ui.screens.menu.drawer;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import fm.doe.national.ui.screens.menu.base.MenuView;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface BaseDrawerView extends MenuView {

    void hideMenu();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToEducationSurveyToolScreen();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToSettingsScreen();

}
