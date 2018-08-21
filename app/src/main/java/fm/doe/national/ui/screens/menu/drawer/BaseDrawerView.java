package fm.doe.national.ui.screens.menu.drawer;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.ArrayList;
import java.util.List;

import fm.doe.national.mock.MockSchool;
import fm.doe.national.ui.screens.base.BaseView;
import fm.doe.national.ui.screens.menu.base.MenuView;

/**
 * Created by Alexander Chibirev on 8/9/2018.
 */

@StateStrategyType(AddToEndSingleStrategy.class)
public interface BaseDrawerView extends MenuView {

    void hideMenu();

    void showEducationSurveyToolScreen();

    void showSettingsScreen();

}
