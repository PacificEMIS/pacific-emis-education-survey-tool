package fm.doe.national.accreditation.di;

import dagger.Component;
import fm.doe.national.accreditation.di.modules.NavigatorModule;
import fm.doe.national.accreditation.di.modules.ProviderModule;
import fm.doe.national.accreditation.ui.navigation.survey_navigator.SurveyNavigator;
import fm.doe.national.accreditation.ui.providers.AccreditationSurveyProvider;
import fm.doe.national.core.di.CoreComponent;

@AccreditationScope
@Component(modules = {
        ProviderModule.class,
        NavigatorModule.class
}, dependencies = {
        CoreComponent.class
})
public interface AccreditationComponent {

    AccreditationSurveyProvider getAccreditationSurveyProvider();

    SurveyNavigator getSurveyNavigator();

}
