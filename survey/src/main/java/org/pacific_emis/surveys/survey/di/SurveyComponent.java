package org.pacific_emis.surveys.survey.di;

import dagger.Component;
import org.pacific_emis.surveys.core.domain.SurveyInteractor;
import org.pacific_emis.surveys.survey.di.modules.ProviderModule;
import org.pacific_emis.surveys.survey_core.ui.survey.SurveyPresenter;

@SurveyScope
@Component(modules = {
        ProviderModule.class
})
public interface SurveyComponent {

    SurveyPresenter getSurveyPresenter();

    SurveyInteractor getSurveyInteractor();

}
