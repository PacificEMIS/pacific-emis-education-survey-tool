package fm.doe.national.ui.screens.region;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import fm.doe.national.core.preferences.entities.AppRegion;
import fm.doe.national.core.ui.screens.base.BaseView;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface ChooseRegionView extends BaseView {

    void setRegions(List<AppRegion> regions);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToMenu();

}
