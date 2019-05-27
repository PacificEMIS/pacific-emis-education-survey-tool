package fm.doe.national.ui.screens.categories;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import java.util.Date;
import java.util.List;

import fm.doe.national.core.data.model.Category;
import fm.doe.national.core.ui.screens.base.BaseView;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface CategoriesView extends BaseView {

    void showCategories(List<Category> categories);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToStandardsScreen(long categoryId);

    void setSurveyDate(Date date);

    void setSchoolName(String schoolName);

}
