package fm.doe.national.wash_core.di.modules;

import org.simpleframework.xml.core.Persister;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.core.data.serialization.SurveyParser;
import fm.doe.national.core.data.serialization.SurveySerializer;
import fm.doe.national.wash_core.data.serialization.serializers.WashSurveyParser;
import fm.doe.national.wash_core.data.serialization.serializers.WashSurveySerializer;
import fm.doe.national.wash_core.di.WashCoreScope;

@Module
public class SerializersModule {

    @Provides
    @WashCoreScope
    public SurveySerializer provideSurveySerializer(Persister persister) {
        return new WashSurveySerializer(persister);
    }

    @Provides
    @WashCoreScope
    public SurveyParser provideSurveyParser(Persister persister) {
        return new WashSurveyParser(persister);
    }
}
