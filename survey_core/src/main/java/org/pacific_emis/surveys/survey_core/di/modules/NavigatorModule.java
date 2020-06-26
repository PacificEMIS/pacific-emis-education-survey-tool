package org.pacific_emis.surveys.survey_core.di.modules;

import dagger.Module;
import dagger.Provides;
import org.pacific_emis.surveys.survey_core.di.SurveyCoreScope;
import org.pacific_emis.surveys.survey_core.navigation.survey_navigator.SurveyNavigatiorImpl;
import org.pacific_emis.surveys.survey_core.navigation.survey_navigator.SurveyNavigator;

@Module
public class NavigatorModule {

    @Provides
    @SurveyCoreScope
    SurveyNavigator provideSurveyNavigator() {
        return new SurveyNavigatiorImpl();
    }

}
