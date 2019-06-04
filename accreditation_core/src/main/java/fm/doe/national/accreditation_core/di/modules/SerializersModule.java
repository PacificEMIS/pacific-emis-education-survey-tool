package fm.doe.national.accreditation_core.di.modules;

import org.simpleframework.xml.core.Persister;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.accreditation_core.data.serializers.AccreditationSurveyParser;
import fm.doe.national.accreditation_core.data.serializers.AccreditationSurveySerializer;
import fm.doe.national.accreditation_core.di.AccreditationCoreScope;
import fm.doe.national.core.data.serialization.SurveyParser;
import fm.doe.national.core.data.serialization.SurveySerializer;

@Module
public class SerializersModule {

    @Provides
    @AccreditationCoreScope
    public SurveySerializer provideSurveySerializer(Persister persister) {
        return new AccreditationSurveySerializer(persister);
    }


    @Provides
    @AccreditationCoreScope
    public SurveyParser provideSurveyParser(Persister persister) {
        return new AccreditationSurveyParser(persister);
    }
}
