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
import fm.doe.national.wash_core.di.WashCoreComponent;

@Module
public class ProviderModule {

    private final SurveyCoreComponent surveyCoreComponent;
    private final CoreComponent coreComponent;
    private final AccreditationCoreComponent accreditationCoreComponent;
    private final WashCoreComponent washCoreComponent;

    public ProviderModule(SurveyCoreComponent surveyCoreComponent,
                          CoreComponent coreComponent,
                          AccreditationCoreComponent accreditationCoreComponent,
                          WashCoreComponent washCoreComponent) {
        this.surveyCoreComponent = surveyCoreComponent;
        this.coreComponent = coreComponent;
        this.accreditationCoreComponent = accreditationCoreComponent;
        this.washCoreComponent = washCoreComponent;
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
                return washCoreComponent.getWashSurveyInteractor();
        }
        throw new IllegalStateException();
    }

}
