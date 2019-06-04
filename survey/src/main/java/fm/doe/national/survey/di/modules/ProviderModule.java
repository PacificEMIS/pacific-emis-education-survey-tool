package fm.doe.national.survey.di.modules;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.accreditation.ui.survey.AccreditationSurveyPresenter;
import fm.doe.national.accreditation_core.di.AccreditationCoreComponent;
import fm.doe.national.core.data.exceptions.NotImplementedException;
import fm.doe.national.core.di.CoreComponent;
import fm.doe.national.core.domain.SurveyInteractor;
import fm.doe.national.survey_core.di.SurveyCoreComponent;
import fm.doe.national.survey_core.ui.survey.SurveyPresenter;

@Module
public class ProviderModule {

    private final SurveyCoreComponent surveyCoreComponent;
    private final CoreComponent coreComponent;
    private final AccreditationCoreComponent accreditationCoreComponent;

    public ProviderModule(SurveyCoreComponent surveyCoreComponent,
                          CoreComponent coreComponent,
                          AccreditationCoreComponent accreditationCoreComponent) {
        this.surveyCoreComponent = surveyCoreComponent;
        this.coreComponent = coreComponent;
        this.accreditationCoreComponent = accreditationCoreComponent;
    }

    @Provides
    SurveyPresenter provideSurveyPresenter() {
        switch (coreComponent.getGlobalPreferences().getSurveyType()) {
            case SCHOOL_ACCREDITATION:
                return new AccreditationSurveyPresenter(accreditationCoreComponent, surveyCoreComponent);
            case WASH:
                throw new NotImplementedException();
        }
        throw new IllegalStateException();
    }

    @Provides
    SurveyInteractor provideSurveyInteractor() {
        switch (coreComponent.getGlobalPreferences().getSurveyType()) {
            case SCHOOL_ACCREDITATION:
                return accreditationCoreComponent.getAccreditationSurveyInteractor();
            case WASH:
                throw new NotImplementedException();
        }
        throw new IllegalStateException();
    }

}
