package org.pacific_emis.surveys.accreditation_core.di.modules;

import dagger.Module;
import dagger.Provides;
import org.pacific_emis.surveys.accreditation_core.data.data_source.AccreditationDataSource;
import org.pacific_emis.surveys.accreditation_core.di.AccreditationCoreScope;
import org.pacific_emis.surveys.accreditation_core.interactors.AccreditationSurveyInteractor;
import org.pacific_emis.surveys.accreditation_core.interactors.AccreditationSurveyInteractorImpl;

@Module
public class InteractorsModule {

    @Provides
    @AccreditationCoreScope
    AccreditationSurveyInteractor provideAccreditationSurveyInteractor(AccreditationDataSource dataSource) {
        return new AccreditationSurveyInteractorImpl(dataSource);
    }
}
