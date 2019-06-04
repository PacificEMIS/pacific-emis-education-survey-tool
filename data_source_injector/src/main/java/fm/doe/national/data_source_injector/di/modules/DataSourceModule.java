package fm.doe.national.data_source_injector.di.modules;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.accreditation_core.di.AccreditationCoreComponent;
import fm.doe.national.core.data.data_source.DataSource;
import fm.doe.national.core.data.exceptions.NotImplementedException;
import fm.doe.national.core.preferences.GlobalPreferences;

@Module
public class DataSourceModule {

    private final AccreditationCoreComponent accreditationCoreComponent;

    public DataSourceModule(AccreditationCoreComponent accreditationCoreComponent) {
        this.accreditationCoreComponent = accreditationCoreComponent;
    }

    @Provides
    DataSource provideDataSource(GlobalPreferences globalPreferences) {
        switch (globalPreferences.getSurveyType()) {
            case SCHOOL_ACCREDITATION:
                return accreditationCoreComponent.getDataSource();
            case WASH:
                throw new NotImplementedException();
        }
        throw new IllegalStateException();
    }

}
