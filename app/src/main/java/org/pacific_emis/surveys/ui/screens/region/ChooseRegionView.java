package org.pacific_emis.surveys.ui.screens.region;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import org.pacific_emis.surveys.core.preferences.entities.AppRegion;
import org.pacific_emis.surveys.core.ui.screens.base.BaseView;

public interface ChooseRegionView extends BaseView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setRegions(List<AppRegion> regions);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToMenu();

}
