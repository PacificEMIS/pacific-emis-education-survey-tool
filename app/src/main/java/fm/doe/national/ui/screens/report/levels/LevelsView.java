package fm.doe.national.ui.screens.report.levels;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import fm.doe.national.ui.screens.base.BaseView;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface LevelsView extends BaseView {

    void setLoadingVisible(boolean visible);

    void setData(SchoolAccreditationLevel data);

}
