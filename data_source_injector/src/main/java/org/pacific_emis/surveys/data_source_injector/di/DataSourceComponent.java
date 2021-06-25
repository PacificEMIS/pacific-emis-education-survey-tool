package org.pacific_emis.surveys.data_source_injector.di;

import org.pacific_emis.surveys.core.data.local_data_source.DataSource;
import org.pacific_emis.surveys.core.data.serialization.SurveyParser;
import org.pacific_emis.surveys.core.data.serialization.SurveySerializer;
import org.pacific_emis.surveys.core.di.CoreComponent;
import org.pacific_emis.surveys.data_source_injector.di.modules.DataSourceModule;
import org.pacific_emis.surveys.data_source_injector.di.modules.SerializersModule;

import dagger.Component;

@DataSourceScope
@Component(modules = {
        DataSourceModule.class,
        SerializersModule.class
}, dependencies = {
        CoreComponent.class
})
public interface DataSourceComponent {

    DataSource getDataRepository();

    SurveyParser getSurveyParser();

    SurveySerializer getSurveySerializer();

}
