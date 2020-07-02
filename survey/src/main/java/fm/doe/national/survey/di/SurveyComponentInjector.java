package fm.doe.national.survey.di;

public class SurveyComponentInjector {
    public static SurveyComponent getComponent(Object injectionSource) {
        if (injectionSource instanceof SurveyComponentProvider) {
            return ((SurveyComponentProvider) injectionSource).provideSurveyComponent();
        } else {
            throw new IllegalStateException("Injection source is not a SurveyComponentProvider");
        }
    }
}
