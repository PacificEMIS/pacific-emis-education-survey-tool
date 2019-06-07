package fm.doe.national.survey_core.ui.survey;

import androidx.annotation.Nullable;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import fm.doe.national.core.data.model.Progress;
import fm.doe.national.core.ui.screens.base.BaseView;
import fm.doe.national.survey_core.navigation.BuildableNavigationItem;
import fm.doe.national.survey_core.navigation.NavigationItem;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface SurveyView extends BaseView {

    void setSchoolName(String schoolName);

    void updateStandardProgress(long standardId, Progress progress);

    void setNavigationTitle(@Nullable Text prefix, Text name, @Nullable Progress progress);

    void setNavigationItems(List<NavigationItem> items);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showNavigationItem(BuildableNavigationItem item);

    void setReportEnabled(boolean enabled);

}
