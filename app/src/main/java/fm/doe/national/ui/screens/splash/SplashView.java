package fm.doe.national.ui.screens.splash;

import android.support.annotation.NonNull;
import android.support.transition.Transition;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import fm.doe.national.ui.screens.menu.base.MenuView;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface SplashView extends MenuView {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void startAnimate(@NonNull Transition transition);

}
