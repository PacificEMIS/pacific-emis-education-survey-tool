package fm.doe.national.app_support.di.modules;

import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;

import java.util.List;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.core.data.model.School;
import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.di.FeatureScope;
import fm.doe.national.data.serialization.parsers.CsvSchoolParser;
import fm.doe.national.data.serialization.parsers.Parser;
import fm.doe.national.data.serialization.parsers.XmlSurveyParser;
import fm.doe.national.data.serialization.serializers.Serializer;
import fm.doe.national.data.serialization.serializers.XmlSurveySerializer;

@Module
public class SerializersModule {
    @Provides
    @FeatureScope
    public Serializer<Survey> provideSurveySerializer() {
        return new XmlSurveySerializer();
    }

    @Provides
    @FeatureScope
    public Parser<Survey> provideSurveyParser() {
        return new XmlSurveyParser();
    }

    @Provides
    @FeatureScope
    public Parser<List<School>> provideSchoolParser() {
        return new CsvSchoolParser();
    }

    @Provides
    @FeatureScope
    public Persister providePersister() {
        return new Persister(new AnnotationStrategy());
    }

}
