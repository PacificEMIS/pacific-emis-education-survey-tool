package org.pacific_emis.surveys.accreditation_core.di;

import dagger.Component;
import org.pacific_emis.surveys.accreditation_core.data.data_source.AccreditationDataSource;
import org.pacific_emis.surveys.accreditation_core.di.modules.DataSourceModule;
import org.pacific_emis.surveys.accreditation_core.di.modules.InteractorsModule;
import org.pacific_emis.surveys.accreditation_core.di.modules.SerializersModule;
import org.pacific_emis.surveys.accreditation_core.interactors.AccreditationSurveyInteractor;
import org.pacific_emis.surveys.core.data.serialization.SurveyParser;
import org.pacific_emis.surveys.core.data.serialization.SurveySerializer;
import org.pacific_emis.surveys.core.di.CoreComponent;

@AccreditationCoreScope
@Component(modules = {
        DataSourceModule.class,
        SerializersModule.class,
        InteractorsModule.class
}, dependencies = {
        CoreComponent.class
})
public interface AccreditationCoreComponent {

    AccreditationDataSource getDataSource();

    SurveyParser getSurveyParser();

    SurveySerializer getSurveySerializer();

    AccreditationSurveyInteractor getAccreditationSurveyInteractor();

}
