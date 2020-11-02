package org.pacific_emis.surveys.wash_core.di;

import dagger.Component;
import org.pacific_emis.surveys.core.data.serialization.SurveyParser;
import org.pacific_emis.surveys.core.data.serialization.SurveySerializer;
import org.pacific_emis.surveys.core.di.CoreComponent;
import org.pacific_emis.surveys.wash_core.data.data_source.WashDataSource;
import org.pacific_emis.surveys.wash_core.di.modules.DataSourceModule;
import org.pacific_emis.surveys.wash_core.di.modules.InteractorsModule;
import org.pacific_emis.surveys.wash_core.di.modules.SerializersModule;
import org.pacific_emis.surveys.wash_core.interactors.WashSurveyInteractor;

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
