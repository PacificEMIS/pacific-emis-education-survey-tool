package fm.doe.national.ui.screens.splash;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import fm.doe.national.ui.screens.base.BaseView;
import fm.doe.national.ui.screens.menu.base.MenuView;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface SplashView extends BaseView {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToSplashEnd();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showLongLoadingProgressBar();
}
