package org.pacific_emis.surveys.survey_core.di;

public class SurveyCoreComponentInjector {
    public static SurveyCoreComponent getComponent(Object injectionSource) {
        if (injectionSource instanceof SurveyCoreComponentProvider) {
            return ((SurveyCoreComponentProvider) injectionSource).provideSurveyCoreComponent();
        } else {
            throw new IllegalStateException("Injection source is not a SurveyCoreComponentProvider");
        }
    }
}
