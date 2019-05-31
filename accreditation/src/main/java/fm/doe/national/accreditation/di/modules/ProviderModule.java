package fm.doe.national.accreditation.di.modules;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.accreditation.di.AccreditationScope;
import fm.doe.national.accreditation.ui.providers.AccreditationSurveyProvider;
import fm.doe.national.accreditation.ui.providers.AccreditationSurveyProviderImpl;

@Module
public class ProviderModule {

    @Provides
    @AccreditationScope
    AccreditationSurveyProvider provideSurveyProvider() {
        return new AccreditationSurveyProviderImpl();
    }

}
