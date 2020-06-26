package org.pacific_emis.surveys.survey_core.ui.survey;

import androidx.annotation.Nullable;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import org.pacific_emis.surveys.core.data.model.Progress;
import org.pacific_emis.surveys.core.ui.screens.base.BaseView;
import org.pacific_emis.surveys.survey_core.navigation.BuildableNavigationItem;
import org.pacific_emis.surveys.survey_core.navigation.NavigationItem;

public interface SurveyView extends BaseView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setSchoolName(String schoolName);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void updateQuestionsGroupProgress(long id, Progress progress);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setNavigationTitle(@Nullable Text prefix, Text name, @Nullable Progress progress);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setNavigationItems(List<NavigationItem> items);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showNavigationItem(BuildableNavigationItem item);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setReportEnabled(boolean enabled);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void close();
}
