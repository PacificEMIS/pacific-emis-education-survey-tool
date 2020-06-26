package org.pacific_emis.surveys.survey_core.navigation.survey_navigator;

import java.util.List;

import org.pacific_emis.surveys.core.data.model.IdentifiedObject;
import org.pacific_emis.surveys.survey_core.navigation.BuildableNavigationItem;
import org.pacific_emis.surveys.survey_core.navigation.NavigationItem;
import org.pacific_emis.surveys.survey_core.ui.survey.SurveyView;

public interface SurveyNavigator {

    void select(BuildableNavigationItem item);

    void select(IdentifiedObject identifiedObject);

    void selectPrevious();

    void selectNext();

    void setViewState(SurveyView surveyView);

    BuildableNavigationItem getCurrentItem();

    void setNavigation(List<NavigationItem> items);

    void close();

}
