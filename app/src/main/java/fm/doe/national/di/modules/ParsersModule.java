package fm.doe.national.di.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.data.parsers.SchoolAccreditationParser;
import fm.doe.national.data.parsers.SchoolAccreditationXmlParser;
import fm.doe.national.data.parsers.SchoolCsvParser;
import fm.doe.national.data.parsers.SchoolParser;

@Module
public class ParsersModule {

    @Provides
    @Singleton
    public SchoolAccreditationParser provideSchoolAccreditationParser() {
        return new SchoolAccreditationXmlParser();
    }

    @Provides
    @Singleton
    public SchoolParser provideSchoolParser() {
        return new SchoolCsvParser();
    }
}
