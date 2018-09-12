package fm.doe.national.ui.screens.standards;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import fm.doe.national.data.data_source.models.Standard;
import fm.doe.national.ui.screens.base.BaseView;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface StandardsView extends BaseView {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToCriteriasScreen(long passingId, long categoryId, long standardId);

    void showStandards(List<Standard> standards);

    void setGlobalProgress(int completed, int total);

    void setSurveyYear(int year);

    void setSchoolName(String schoolName);
}
