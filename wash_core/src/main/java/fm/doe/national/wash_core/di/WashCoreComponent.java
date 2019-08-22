package fm.doe.national.wash_core.di;

import dagger.Component;
import fm.doe.national.core.data.serialization.SurveyParser;
import fm.doe.national.core.data.serialization.SurveySerializer;
import fm.doe.national.core.di.CoreComponent;
import fm.doe.national.wash_core.data.data_source.WashDataSource;
import fm.doe.national.wash_core.di.modules.DataSourceModule;
import fm.doe.national.wash_core.di.modules.InteractorsModule;
import fm.doe.national.wash_core.di.modules.SerializersModule;
import fm.doe.national.wash_core.interactors.WashSurveyInteractor;

@WashCoreScope
@Component(modules = {
        DataSourceModule.class,
        SerializersModule.class,
        InteractorsModule.class
}, dependencies = {
        CoreComponent.class
})
public interface WashCoreComponent {

    WashDataSource getDataSource();

    SurveyParser getSurveyParser();

    SurveySerializer getSurveySerializer();

    WashSurveyInteractor getWashSurveyInteractor();

}
