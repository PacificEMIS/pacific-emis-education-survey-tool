package fm.doe.national.ui.screens.authentication;

import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import fm.doe.national.core.ui.screens.base.BaseView;

@StateStrategyType(OneExecutionStateStrategy.class)
public interface AuthenticationView extends BaseView {

    void navigateToRegionChoose();

}
