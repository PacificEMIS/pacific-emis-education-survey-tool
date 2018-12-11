package fm.doe.national.di.modules;

import org.simpleframework.xml.core.Persister;

import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.data.data_source.models.School;
import fm.doe.national.data.data_source.models.serializable.LinkedSchoolAccreditation;
import fm.doe.national.data.parsers.CsvSchoolParser;
import fm.doe.national.data.parsers.Parser;
import fm.doe.national.data.parsers.XmlSchoolAccreditationParser;

@Module
public class ParsersModule {

    @Provides
    @Singleton
    public Parser<LinkedSchoolAccreditation> provideSchoolAccreditationParser() {
        return new XmlSchoolAccreditationParser();
    }

    @Provides
    @Singleton
    public Parser<List<School>> provideSchoolParser() {
        return new CsvSchoolParser();
    }

    @Provides
    @Singleton
    public Persister providePersister() {
        return new Persister();
    }
}
