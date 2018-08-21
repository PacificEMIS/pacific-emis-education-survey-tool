package fm.doe.national.ui.screens.shool_accreditation;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.ArrayList;
import java.util.List;

import fm.doe.national.mock.MockSchool;
import fm.doe.national.mock.MockStandard;
import fm.doe.national.ui.screens.menu.base.MenuView;
import fm.doe.national.ui.screens.menu.drawer.BaseDrawerView;

/**
 * Created by Alexander Chibirev on 8/10/2018.
 */

@StateStrategyType(AddToEndSingleStrategy.class)
interface SchoolAccreditationView extends BaseDrawerView {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showChooseCategoryScreen(List<MockStandard> standards);


    void setSchools(ArrayList<MockSchool> schools);

}
