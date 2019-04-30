package fm.doe.national.di.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.data.data_source.models.serializable.LinkedSchoolAccreditation;
import fm.doe.national.data.serialization.serializers.Serializer;
import fm.doe.national.data.serialization.serializers.XmlSchoolAccreditationSerializer;

@Module
public class SerializersModule {
    @Provides
    @Singleton
    public Serializer<LinkedSchoolAccreditation> provideSchoolAccreditationSerializer() {
        return new XmlSchoolAccreditationSerializer();
    }
}
