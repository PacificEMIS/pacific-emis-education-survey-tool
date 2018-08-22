package fm.doe.national.di.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.data.parsers.CsvSchoolParser;
import fm.doe.national.data.parsers.SchoolAccreditationParser;
import fm.doe.national.data.parsers.SchoolParser;
import fm.doe.national.data.parsers.XmlSchoolAccreditationParser;

@Module
public class ParsersModule {

    @Provides
    @Singleton
    public SchoolAccreditationParser provideSchoolAccreditationParser() {
        return new XmlSchoolAccreditationParser();
    }

    @Provides
    @Singleton
    public SchoolParser provideSchoolParser() {
        return new CsvSchoolParser();
    }
}
