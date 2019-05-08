package fm.doe.national.ui.screens.categories;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import java.util.Date;
import java.util.List;

import fm.doe.national.data.model.Category;
import fm.doe.national.ui.custom_views.summary.SummaryViewData;
import fm.doe.national.ui.screens.base.BaseView;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface CategoriesView extends BaseView {

    void showCategories(List<Category> categories);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToStandardsScreen(long passingId, long categoryId);

    void setSurveyDate(Date date);

    void setSchoolName(String schoolName);

    void setSummaryData(List<SummaryViewData> data);

    void showSummaryLoading();

    void hideSummaryLoading();
}
