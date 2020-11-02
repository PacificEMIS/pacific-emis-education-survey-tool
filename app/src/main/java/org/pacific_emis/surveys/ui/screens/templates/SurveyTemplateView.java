package org.pacific_emis.surveys.ui.screens.templates;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import org.pacific_emis.surveys.core.ui.screens.base.BaseView;
import org.pacific_emis.surveys.survey_core.navigation.NavigationItem;

public interface SurveyTemplateView extends BaseView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setItems(List<NavigationItem> items);

}
