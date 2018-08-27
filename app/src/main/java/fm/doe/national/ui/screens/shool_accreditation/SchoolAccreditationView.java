package fm.doe.national.ui.screens.shool_accreditation;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import fm.doe.national.data.data_source.models.SchoolAccreditationPassing;
import fm.doe.national.ui.screens.menu.drawer.BaseDrawerView;

/**
 * Created by Alexander Chibirev on 8/10/2018.
 */

@StateStrategyType(AddToEndSingleStrategy.class)
interface SchoolAccreditationView extends BaseDrawerView {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showChooseCategoryScreen(SchoolAccreditationPassing schoolAccreditationPassing);


    void setAccreditations(List<SchoolAccreditationPassing> accreditations);

}
