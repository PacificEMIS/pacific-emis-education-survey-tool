package org.pacific_emis.surveys.data_source_injector.di.modules;

import org.pacific_emis.surveys.accreditation_core.di.AccreditationCoreComponent;
import org.pacific_emis.surveys.core.data.data_repository.DataRepository;
import org.pacific_emis.surveys.core.data.local_data_source.DataSource;
import org.pacific_emis.surveys.core.di.CoreComponent;
import org.pacific_emis.surveys.core.preferences.LocalSettings;
import org.pacific_emis.surveys.wash_core.di.WashCoreComponent;

import dagger.Module;
import dagger.Provides;

@Module
public class DataSourceModule {

    private final CoreComponent coreComponent;
    private final AccreditationCoreComponent accreditationCoreComponent;
    private final WashCoreComponent washCoreComponent;

    public DataSourceModule(
            CoreComponent coreComponent,
            AccreditationCoreComponent accreditationCoreComponent,
            WashCoreComponent washCoreComponent
    ) {
        this.coreComponent = coreComponent;
        this.accreditationCoreComponent = accreditationCoreComponent;
        this.washCoreComponent = washCoreComponent;
    }

    @Provides
    DataSource provideDataRepository(LocalSettings localSettings) {
        switch (localSettings.getSurveyTypeOrDefault()) {
            case SCHOOL_ACCREDITATION:
                return new DataRepository(coreComponent.getRemoteDataSource(), accreditationCoreComponent.getDataSource());
//                return new DataRepository(accreditationCoreComponent.getDataSource());
            case WASH:
                return new DataRepository(coreComponent.getRemoteDataSource(), washCoreComponent.getDataSource());
//                return new DataRepository(washCoreComponent.getDataSource());
        }
        throw new IllegalStateException();
    }

}
