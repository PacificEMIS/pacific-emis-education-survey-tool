package fm.doe.national.ui.screens.choose_category;

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import fm.doe.national.data.data_source.models.Standard;
import fm.doe.national.ui.screens.base.BaseView;

public interface ChooseCategoryView extends BaseView {

    void setCategories(List<Standard> standards);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showStandardScreen(long passingId, int position);

}
