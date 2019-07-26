package fm.doe.national.ui.screens.templates;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import fm.doe.national.core.ui.screens.base.BaseView;
import fm.doe.national.survey_core.navigation.NavigationItem;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface SurveyTemplateView extends BaseView {

    void setItems(List<NavigationItem> items);

}
