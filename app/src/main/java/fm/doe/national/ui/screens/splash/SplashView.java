package fm.doe.national.ui.screens.splash;

import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import fm.doe.national.core.ui.screens.base.BaseView;

public interface SplashView extends BaseView {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToMasterPassword();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToRegionChoose();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToMenu();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void requestAppPermissions();

}
