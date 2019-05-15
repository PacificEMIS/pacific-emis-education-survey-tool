package fm.doe.national.ui.screens.splash_end;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import fm.doe.national.ui.screens.menu.base.MenuView;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface SplashEndView extends MenuView {
}
