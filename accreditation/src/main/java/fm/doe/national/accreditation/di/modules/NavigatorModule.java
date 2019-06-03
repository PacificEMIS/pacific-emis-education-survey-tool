package fm.doe.national.accreditation.di.modules;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.accreditation.di.AccreditationScope;
import fm.doe.national.accreditation.ui.navigation.survey_navigator.SurveyNavigatiorImpl;
import fm.doe.national.accreditation.ui.navigation.survey_navigator.SurveyNavigator;

@Module
public class NavigatorModule {

    @Provides
    @AccreditationScope
    SurveyNavigator provideSurveyNavigator() {
        return new SurveyNavigatiorImpl();
    }

}
