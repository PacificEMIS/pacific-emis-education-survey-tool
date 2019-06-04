package fm.doe.national.data_source_injector.di;

import dagger.Component;
import fm.doe.national.core.data.data_source.DataSource;
import fm.doe.national.core.data.serialization.SurveyParser;
import fm.doe.national.core.data.serialization.SurveySerializer;
import fm.doe.national.core.di.CoreComponent;
import fm.doe.national.data_source_injector.di.modules.DataSourceModule;
import fm.doe.national.data_source_injector.di.modules.SerializersModule;

@DataSourceScope
@Component(modules = {
        DataSourceModule.class,
        SerializersModule.class
}, dependencies = {
        CoreComponent.class
})
public interface DataSourceComponent {

    DataSource getDataSource();

    SurveyParser getSurveyParser();

    SurveySerializer getSurveySerializer();

}
