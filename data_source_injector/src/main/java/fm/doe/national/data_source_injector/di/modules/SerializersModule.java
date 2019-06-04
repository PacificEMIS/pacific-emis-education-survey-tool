package fm.doe.national.data_source_injector.di.modules;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.accreditation_core.di.AccreditationCoreComponent;
import fm.doe.national.core.data.exceptions.NotImplementedException;
import fm.doe.national.core.data.serialization.SurveyParser;
import fm.doe.national.core.data.serialization.SurveySerializer;
import fm.doe.national.core.preferences.GlobalPreferences;

@Module
public class SerializersModule {

    private final AccreditationCoreComponent accreditationCoreComponent;

    public SerializersModule(AccreditationCoreComponent accreditationCoreComponent) {
        this.accreditationCoreComponent = accreditationCoreComponent;
    }

    @Provides
    public SurveyParser provideSurveyParser(GlobalPreferences globalPreferences) {
        switch (globalPreferences.getSurveyType()) {
            case SCHOOL_ACCREDITATION:
                return accreditationCoreComponent.getSurveyParser();
            case WASH:
                throw new NotImplementedException();
        }
        throw new IllegalStateException();
    }

    @Provides
    public SurveySerializer provideSurveySerializer(GlobalPreferences globalPreferences) {
        switch (globalPreferences.getSurveyType()) {
            case SCHOOL_ACCREDITATION:
                return accreditationCoreComponent.getSurveySerializer();
            case WASH:
                throw new NotImplementedException();
        }
        throw new IllegalStateException();
    }

}
