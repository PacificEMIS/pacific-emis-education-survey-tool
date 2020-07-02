package fm.doe.national.survey_core.di.modules;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.survey_core.di.SurveyCoreScope;
import fm.doe.national.survey_core.navigation.survey_navigator.SurveyNavigatiorImpl;
import fm.doe.national.survey_core.navigation.survey_navigator.SurveyNavigator;

@Module
public class NavigatorModule {

    @Provides
    @SurveyCoreScope
    SurveyNavigator provideSurveyNavigator() {
        return new SurveyNavigatiorImpl();
    }

}
