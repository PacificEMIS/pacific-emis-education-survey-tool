package fm.doe.national.core.di.modules;

import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;

import java.util.List;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.core.data.model.School;
import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.data.serialization.parsers.CsvSchoolParser;
import fm.doe.national.core.data.serialization.parsers.Parser;
import fm.doe.national.core.data.serialization.parsers.XmlSurveyParser;
import fm.doe.national.core.data.serialization.serializers.Serializer;
import fm.doe.national.core.data.serialization.serializers.XmlSurveySerializer;
import fm.doe.national.core.di.CoreScope;

@Module
public class SerializersModule {
    @Provides
    @CoreScope
    public Serializer<Survey> provideSurveySerializer(Persister persister) {
        return new XmlSurveySerializer(persister);
    }

    @Provides
    @CoreScope
    public Parser<Survey> provideSurveyParser(Persister persister) {
        return new XmlSurveyParser(persister);
    }

    @Provides
    @CoreScope
    public Parser<List<School>> provideSchoolParser() {
        return new CsvSchoolParser();
    }

    @Provides
    @CoreScope
    public Persister providePersister() {
        return new Persister(new AnnotationStrategy());
    }

}
