package fm.doe.national.ui.screens.menu.drawer;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import fm.doe.national.ui.screens.menu.base.BaseMenuView;

/**
 * Created by Alexander Chibirev on 8/9/2018.
 */

@StateStrategyType(AddToEndSingleStrategy.class)
public interface BaseDrawerView extends BaseMenuView {

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
