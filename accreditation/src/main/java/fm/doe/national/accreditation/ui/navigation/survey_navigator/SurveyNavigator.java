package fm.doe.national.accreditation.ui.navigation.survey_navigator;

import fm.doe.national.accreditation.ui.navigation.BuildableNavigationItem;
import fm.doe.national.accreditation.ui.survey.SurveyView;

public interface SurveyNavigator {

    void select(BuildableNavigationItem item);

    void selectPrevious();

    void selectNext();

    void setViewState(SurveyView surveyView);

    BuildableNavigationItem getCurrentItem();

}
