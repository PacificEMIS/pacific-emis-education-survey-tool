package fm.doe.national.ui.screens.choose_category;

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import fm.doe.national.data.data_source.models.SchoolAccreditationResult;
import fm.doe.national.mock.MockStandard;
import fm.doe.national.ui.screens.base.BaseView;

/**
 * Created by Alexander Chibirev on 8/17/2018.
 */

public interface ChooseCategoryView extends BaseView {

    void setCategories(List<MockStandard> standards);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showStandardScreen(SchoolAccreditationResult standard);

}
