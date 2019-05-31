package fm.doe.national.accreditation.di;

import dagger.Component;
import fm.doe.national.accreditation.di.modules.ProviderModule;
import fm.doe.national.accreditation.ui.providers.AccreditationSurveyProvider;
import fm.doe.national.core.di.CoreComponent;

@AccreditationScope
@Component(modules = {
        ProviderModule.class
}, dependencies = {
        CoreComponent.class
})
public interface AccreditationComponent {

    AccreditationSurveyProvider getAccreditationSurveyProvider();

}
