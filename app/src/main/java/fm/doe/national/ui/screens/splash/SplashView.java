package fm.doe.national.ui.screens.splash;


import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import fm.doe.national.core.ui.screens.base.BaseView;

@StateStrategyType(OneExecutionStateStrategy.class)
public interface SplashView extends BaseView {

    void navigateToMasterPassword();

    void navigateToRegionChoose();

    void navigateToMenu();

}
