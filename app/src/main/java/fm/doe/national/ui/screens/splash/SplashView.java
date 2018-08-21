package fm.doe.national.ui.screens.splash;

import android.support.annotation.NonNull;
import android.support.transition.Transition;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import fm.doe.national.data.data_source.models.School;
import fm.doe.national.ui.screens.menu.base.MenuView;

/**
 * Created by Alexander Chibirev on 8/10/2018.
 */

@StateStrategyType(AddToEndSingleStrategy.class)
public interface SplashView extends MenuView {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void startAnimate(@NonNull Transition transition);

}
