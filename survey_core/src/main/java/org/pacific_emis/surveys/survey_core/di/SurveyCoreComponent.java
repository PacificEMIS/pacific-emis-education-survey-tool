package org.pacific_emis.surveys.survey_core.di;

import dagger.Component;
import org.pacific_emis.surveys.core.di.CoreComponent;
import org.pacific_emis.surveys.survey_core.di.modules.NavigatorModule;
import org.pacific_emis.surveys.survey_core.navigation.survey_navigator.SurveyNavigator;

@SurveyCoreScope
@Component(modules = {
        NavigatorModule.class
}, dependencies = {
        CoreComponent.class
})
public interface SurveyCoreComponent {

    SurveyNavigator getSurveyNavigator();

}
