package org.pacific_emis.surveys.survey.di.modules;

import dagger.Module;
import dagger.Provides;
import org.pacific_emis.surveys.accreditation.ui.survey.AccreditationSurveyPresenter;
import org.pacific_emis.surveys.accreditation_core.di.AccreditationCoreComponent;
import org.pacific_emis.surveys.core.di.CoreComponent;
import org.pacific_emis.surveys.core.domain.SurveyInteractor;
import org.pacific_emis.surveys.survey_core.di.SurveyCoreComponent;
import org.pacific_emis.surveys.survey_core.ui.survey.SurveyPresenter;
import org.pacific_emis.surveys.wash.ui.survey.WashSurveyPresenter;
import org.pacific_emis.surveys.wash_core.di.WashCoreComponent;

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
        switch (coreComponent.getLocalSettings().getSurveyType()) {
            case SCHOOL_ACCREDITATION:
                return new AccreditationSurveyPresenter(accreditationCoreComponent, surveyCoreComponent);
            case WASH:
                return new WashSurveyPresenter(washCoreComponent, surveyCoreComponent);
        }
        throw new IllegalStateException();
    }

    @Provides
    SurveyInteractor provideSurveyInteractor() {
        switch (coreComponent.getLocalSettings().getSurveyType()) {
            case SCHOOL_ACCREDITATION:
                return accreditationCoreComponent.getAccreditationSurveyInteractor();
            case WASH:
                return washCoreComponent.getWashSurveyInteractor();
        }
        throw new IllegalStateException();
    }

}
