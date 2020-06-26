package org.pacific_emis.surveys.ui.screens.password;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import org.pacific_emis.surveys.core.ui.screens.base.BaseView;

public interface PasswordView extends BaseView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setPasswordsNotMatchVisible(boolean visible);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setPasswordTooShortVisible(boolean visible);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToRegion();

}
