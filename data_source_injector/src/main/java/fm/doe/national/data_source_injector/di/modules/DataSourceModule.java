package fm.doe.national.data_source_injector.di.modules;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.accreditation_core.di.AccreditationCoreComponent;
import fm.doe.national.core.data.data_source.DataSource;
import fm.doe.national.core.preferences.GlobalPreferences;
import fm.doe.national.wash_core.di.WashCoreComponent;

@Module
public class DataSourceModule {

    private final AccreditationCoreComponent accreditationCoreComponent;
    private final WashCoreComponent washCoreComponent;

    public DataSourceModule(AccreditationCoreComponent accreditationCoreComponent, WashCoreComponent washCoreComponent) {
        this.accreditationCoreComponent = accreditationCoreComponent;
        this.washCoreComponent = washCoreComponent;
    }

    @Provides
    DataSource provideDataSource(GlobalPreferences globalPreferences) {
        switch (globalPreferences.getSurveyTypeOrDefault()) {
            case SCHOOL_ACCREDITATION:
                return accreditationCoreComponent.getDataSource();
            case WASH:
                return washCoreComponent.getDataSource();
        }
        throw new IllegalStateException();
    }

}
