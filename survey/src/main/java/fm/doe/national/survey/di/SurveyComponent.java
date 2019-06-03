package fm.doe.national.survey.di;

import dagger.Component;
import fm.doe.national.survey.di.modules.ProviderModule;
import fm.doe.national.survey_core.ui.survey.SurveyPresenter;

@SurveyScope
@Component(modules = {
        ProviderModule.class
})
public interface SurveyComponent {

    SurveyPresenter getSurveyPresenter();

}
