package fm.doe.national.core.di.modules;

import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;

import java.util.List;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.core.data.model.School;
import fm.doe.national.core.data.serialization.CsvSchoolParser;
import fm.doe.national.core.data.serialization.Parser;
import fm.doe.national.core.di.CoreScope;

@Module
public class SerializationModule {

    @Provides
    @CoreScope
    public Persister providePersister() {
        return new Persister(new AnnotationStrategy());
    }

    @Provides
    @CoreScope
    public Parser<List<School>> provideSchoolParser() {
        return new CsvSchoolParser();
    }
}
