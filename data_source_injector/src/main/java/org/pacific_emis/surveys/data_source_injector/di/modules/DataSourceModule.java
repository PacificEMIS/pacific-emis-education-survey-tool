package org.pacific_emis.surveys.data_source_injector.di.modules;

import dagger.Module;
import dagger.Provides;
import org.pacific_emis.surveys.accreditation_core.di.AccreditationCoreComponent;
import org.pacific_emis.surveys.core.data.data_source.DataSource;
import org.pacific_emis.surveys.core.preferences.LocalSettings;
import org.pacific_emis.surveys.wash_core.di.WashCoreComponent;

@Module
public class DataSourceModule {

    private final AccreditationCoreComponent accreditationCoreComponent;
    private final WashCoreComponent washCoreComponent;

    public DataSourceModule(AccreditationCoreComponent accreditationCoreComponent, WashCoreComponent washCoreComponent) {
        this.accreditationCoreComponent = accreditationCoreComponent;
        this.washCoreComponent = washCoreComponent;
    }

    @Provides
    DataSource provideDataSource(LocalSettings localSettings) {
        switch (localSettings.getSurveyTypeOrDefault()) {
            case SCHOOL_ACCREDITATION:
                return accreditationCoreComponent.getDataSource();
            case WASH:
                return washCoreComponent.getDataSource();
        }
        throw new IllegalStateException();
    }

}
