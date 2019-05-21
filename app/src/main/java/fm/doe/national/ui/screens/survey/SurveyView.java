package fm.doe.national.ui.screens.survey;

import androidx.annotation.Nullable;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import fm.doe.national.data.model.Progress;
import fm.doe.national.ui.screens.base.BaseView;
import fm.doe.national.ui.screens.survey.navigation.NavigationItem;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface SurveyView extends BaseView {

    void setSchoolName(String schoolName);

    void setNavigationTitle(@Nullable Text prefix, Text name, @Nullable Progress progress);

    void setNavigationItems(List<NavigationItem> items);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showNavigationItem(NavigationItem item);
}
