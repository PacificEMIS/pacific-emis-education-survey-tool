package fm.doe.national.ui.screens.password;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import fm.doe.national.core.ui.screens.base.BaseView;

public interface PasswordView extends BaseView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setPasswordsNotMatchVisible(boolean visible);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToSignIn();

}
