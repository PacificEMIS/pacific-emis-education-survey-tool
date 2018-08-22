package fm.doe.national.di.modules;

import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.data.data_source.models.School;
import fm.doe.national.data.data_source.models.SchoolAccreditation;
import fm.doe.national.data.parsers.CsvSchoolParser;
import fm.doe.national.data.parsers.Parser;
import fm.doe.national.data.parsers.SchoolParser;
import fm.doe.national.data.parsers.XmlSchoolAccreditationParser;

@Module
public class ParsersModule {

    @Provides
    @Singleton
    public Parser<SchoolAccreditation> provideSchoolAccreditationParser() {
        return new XmlSchoolAccreditationParser();
    }

    @Provides
    @Singleton
    public Parser<List<School>> provideSchoolParser() {
        return new CsvSchoolParser();
    }
}
