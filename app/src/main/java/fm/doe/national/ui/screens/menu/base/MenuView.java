package fm.doe.national.ui.screens.menu.base;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import fm.doe.national.core.ui.screens.base.BaseView;
import fm.doe.national.ui.screens.menu.MainMenuItem;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface MenuView extends BaseView {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToSchoolAccreditationScreen();

    void setItems(List<MainMenuItem> items);

    void setLogo(String path);
}
