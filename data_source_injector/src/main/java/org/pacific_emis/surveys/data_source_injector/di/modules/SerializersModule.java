package org.pacific_emis.surveys.data_source_injector.di.modules;

import dagger.Module;
import dagger.Provides;
import org.pacific_emis.surveys.accreditation_core.di.AccreditationCoreComponent;
import org.pacific_emis.surveys.core.data.serialization.SurveyParser;
import org.pacific_emis.surveys.core.data.serialization.SurveySerializer;
import org.pacific_emis.surveys.core.preferences.LocalSettings;
import org.pacific_emis.surveys.wash_core.di.WashCoreComponent;

@Module
public class SerializersModule {

    private final AccreditationCoreComponent accreditationCoreComponent;
    private final WashCoreComponent washCoreComponent;

    public SerializersModule(AccreditationCoreComponent accreditationCoreComponent, WashCoreComponent washCoreComponent) {
        this.accreditationCoreComponent = accreditationCoreComponent;
        this.washCoreComponent = washCoreComponent;
    }

    @Provides
    public SurveyParser provideSurveyParser(LocalSettings localSettings) {
        switch (localSettings.getSurveyTypeOrDefault()) {
            case SCHOOL_ACCREDITATION:
                return accreditationCoreComponent.getSurveyParser();
            case WASH:
                return washCoreComponent.getSurveyParser();
        }
        throw new IllegalStateException();
    }

    @Provides
    public SurveySerializer provideSurveySerializer(LocalSettings localSettings) {
        switch (localSettings.getSurveyTypeOrDefault()) {
            case SCHOOL_ACCREDITATION:
                return accreditationCoreComponent.getSurveySerializer();
            case WASH:
                return washCoreComponent.getSurveySerializer();
        }
        throw new IllegalStateException();
    }

}
