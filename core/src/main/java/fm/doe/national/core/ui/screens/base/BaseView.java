package fm.doe.national.core.ui.screens.base;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.MvpView;
import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;


@StateStrategyType(AddToEndSingleStrategy.class)
public interface BaseView extends MvpView {

    void showToast(Text text);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showMessage(Text title, Text message);

    void showWaiting();

    void hideWaiting();

    void promptMasterPassword(Text title);
}
