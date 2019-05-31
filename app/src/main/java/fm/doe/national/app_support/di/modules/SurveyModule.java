package fm.doe.national.app_support.di.modules;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.accreditation.di.AccreditationComponent;
import fm.doe.national.core.data.exceptions.NotImplementedException;
import fm.doe.national.core.preferences.GlobalPreferences;
import fm.doe.national.core.ui.providers.SurveyProvider;

@Module
public class SurveyModule {

    private final AccreditationComponent accreditationComponent;

    public SurveyModule(AccreditationComponent accreditationComponent) {
        this.accreditationComponent = accreditationComponent;
    }

    @Provides
    SurveyProvider provideSurveyProvider(GlobalPreferences globalPreferences) {
        switch (globalPreferences.getSurveyType()) {
            case ACCREDITATION:
                return accreditationComponent.getAccreditationSurveyProvider();
            case WASH:
                // TODO: not implemented
                throw new NotImplementedException();
        }
        throw new IllegalStateException();
    }
}
