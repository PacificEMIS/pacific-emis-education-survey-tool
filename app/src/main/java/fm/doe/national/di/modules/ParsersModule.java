package fm.doe.national.di.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.data.parsers.SchoolAccreditationParser;
import fm.doe.national.data.parsers.SchoolAccreditationXmlParser;

@Module
public class ParsersModule {

    @Provides
    @Singleton
    public SchoolAccreditationParser provideSchoolAccreditationParser() {
        return new SchoolAccreditationXmlParser();
    }
}
