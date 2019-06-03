package fm.doe.national.survey_core.navigation.survey_navigator;

import fm.doe.national.survey_core.navigation.BuildableNavigationItem;
import fm.doe.national.survey_core.ui.survey.SurveyView;

public interface SurveyNavigator {

    void select(BuildableNavigationItem item);

    void selectPrevious();

    void selectNext();

    void setViewState(SurveyView surveyView);

    BuildableNavigationItem getCurrentItem();

}
