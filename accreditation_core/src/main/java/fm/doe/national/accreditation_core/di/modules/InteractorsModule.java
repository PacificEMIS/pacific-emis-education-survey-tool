package fm.doe.national.accreditation_core.di.modules;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.accreditation_core.data.data_source.AccreditationDataSource;
import fm.doe.national.accreditation_core.di.AccreditationCoreScope;
import fm.doe.national.accreditation_core.interactors.AccreditationSurveyInteractor;
import fm.doe.national.accreditation_core.interactors.AccreditationSurveyInteractorImpl;

@Module
public class InteractorsModule {

    @Provides
    @AccreditationCoreScope
    AccreditationSurveyInteractor provideAccreditationSurveyInteractor(AccreditationDataSource dataSource) {
        return new AccreditationSurveyInteractorImpl(dataSource);
    }
}
