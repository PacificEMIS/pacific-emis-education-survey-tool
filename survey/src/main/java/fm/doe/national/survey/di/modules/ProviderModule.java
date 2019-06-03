package fm.doe.national.survey.di.modules;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.accreditation.ui.survey.AccreditationSurveyPresenter;
import fm.doe.national.core.data.exceptions.NotImplementedException;
import fm.doe.national.core.di.CoreComponent;
import fm.doe.national.survey_core.di.SurveyCoreComponent;
import fm.doe.national.survey_core.ui.survey.SurveyPresenter;

@Module
public class ProviderModule {

    private final SurveyCoreComponent surveyCoreComponent;
    private final CoreComponent coreComponent;

    public ProviderModule(SurveyCoreComponent surveyCoreComponent, CoreComponent coreComponent) {
        this.surveyCoreComponent = surveyCoreComponent;
        this.coreComponent = coreComponent;
    }

    @Provides
    SurveyPresenter provideSurveyPresenter() {
        switch (coreComponent.getGlobalPreferences().getSurveyType()) {
            case ACCREDITATION:
                return new AccreditationSurveyPresenter(coreComponent, surveyCoreComponent);
            case WASH:
                throw new NotImplementedException();
        }
        throw new IllegalStateException();
    }

}
