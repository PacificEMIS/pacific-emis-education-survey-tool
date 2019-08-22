package fm.doe.national.accreditation_core.di;

import dagger.Component;
import fm.doe.national.accreditation_core.data.data_source.AccreditationDataSource;
import fm.doe.national.accreditation_core.di.modules.DataSourceModule;
import fm.doe.national.accreditation_core.di.modules.InteractorsModule;
import fm.doe.national.accreditation_core.di.modules.SerializersModule;
import fm.doe.national.accreditation_core.interactors.AccreditationSurveyInteractor;
import fm.doe.national.core.data.serialization.SurveyParser;
import fm.doe.national.core.data.serialization.SurveySerializer;
import fm.doe.national.core.di.CoreComponent;

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
