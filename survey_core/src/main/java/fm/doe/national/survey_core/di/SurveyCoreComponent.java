package fm.doe.national.survey_core.di;

import dagger.Component;
import fm.doe.national.core.di.CoreComponent;
import fm.doe.national.survey_core.di.modules.NavigatorModule;
import fm.doe.national.survey_core.navigation.survey_navigator.SurveyNavigator;

@SurveyCoreScope
@Component(modules = {
        NavigatorModule.class
}, dependencies = {
        CoreComponent.class
})
public interface SurveyCoreComponent {

    SurveyNavigator getSurveyNavigator();

}
