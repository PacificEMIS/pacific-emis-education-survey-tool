package org.pacific_emis.surveys.data_source_injector.di;

import org.pacific_emis.surveys.core.data.data_source.DataSource;
import org.pacific_emis.surveys.core.data.serialization.SurveyParser;
import org.pacific_emis.surveys.core.data.serialization.SurveySerializer;
import org.pacific_emis.surveys.core.di.CoreComponent;
import org.pacific_emis.surveys.data_source_injector.di.modules.DataSourceModule;
import org.pacific_emis.surveys.data_source_injector.di.modules.SerializersModule;
import org.pacific_emis.surveys.remote_data.RemoteData;
import org.pacific_emis.surveys.remote_data.di.RemoteDataModule;

import dagger.Component;

@DataSourceScope
@Component(modules = {
        DataSourceModule.class,
        SerializersModule.class,
        RemoteDataModule.class
}, dependencies = {
        CoreComponent.class
})
public interface DataSourceComponent {

    DataSource getDataSource();

    RemoteData getRemoteDataSource();

    SurveyParser getSurveyParser();

    SurveySerializer getSurveySerializer();

}
