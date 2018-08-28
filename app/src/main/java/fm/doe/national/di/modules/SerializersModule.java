package fm.doe.national.di.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.data.data_source.models.SchoolAccreditation;
import fm.doe.national.data.serializers.Serializer;
import fm.doe.national.data.serializers.XmlSchoolAccreditationSerializer;

@Module
public class SerializersModule {
    @Provides
    @Singleton
    public Serializer<SchoolAccreditation> provideSchoolAccreditationSerializer() {
        return new XmlSchoolAccreditationSerializer();
    }
}
