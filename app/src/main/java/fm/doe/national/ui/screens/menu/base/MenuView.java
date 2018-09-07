package fm.doe.national.ui.screens.menu.base;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.omega_r.libs.omegatypes.Text;

import java.util.List;

import fm.doe.national.ui.screens.base.BaseView;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface MenuView extends BaseView {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToSchoolAccreditationScreen();

    void setItems(List<Text> items);
}
