package fm.doe.national.data_source_injector.di.modules;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.accreditation_core.di.AccreditationCoreComponent;
import fm.doe.national.core.data.serialization.SurveyParser;
import fm.doe.national.core.data.serialization.SurveySerializer;
import fm.doe.national.core.preferences.LocalSettings;
import fm.doe.national.wash_core.di.WashCoreComponent;

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
