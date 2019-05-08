package fm.doe.national.ui.screens.splash;


import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import fm.doe.national.ui.screens.base.BaseView;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface SplashStartView extends BaseView {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToSplashEnd();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showLongLoadingProgressBar();
}
