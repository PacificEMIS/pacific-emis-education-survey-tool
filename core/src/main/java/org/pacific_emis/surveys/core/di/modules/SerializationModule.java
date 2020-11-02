package org.pacific_emis.surveys.core.di.modules;

import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;

import java.util.List;

import dagger.Module;
import dagger.Provides;
import org.pacific_emis.surveys.core.data.model.School;
import org.pacific_emis.surveys.core.data.serialization.CsvSchoolParser;
import org.pacific_emis.surveys.core.data.serialization.Parser;
import org.pacific_emis.surveys.core.di.CoreScope;

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
