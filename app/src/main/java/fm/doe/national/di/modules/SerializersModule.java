package fm.doe.national.di.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.data.model.Survey;
import fm.doe.national.data.serialization.serializers.Serializer;
import fm.doe.national.data.serialization.serializers.XmlSurveySerializer;

@Module
public class SerializersModule {
    @Provides
    @Singleton
    public Serializer<Survey> provideSchoolAccreditationSerializer() {
        return new XmlSurveySerializer();
    }
}
