package org.pacific_emis.surveys.app_support.di.modules;

import android.content.res.AssetManager;

import java.util.List;

import dagger.Module;
import dagger.Provides;
import org.pacific_emis.surveys.accreditation_core.di.AccreditationCoreComponent;
import org.pacific_emis.surveys.core.data.model.School;
import org.pacific_emis.surveys.core.data.serialization.Parser;
import org.pacific_emis.surveys.core.preferences.LocalSettings;
import org.pacific_emis.surveys.data_source_injector.di.DataSourceComponent;
import org.pacific_emis.surveys.domain.SettingsInteractor;
import org.pacific_emis.surveys.remote_storage.di.RemoteStorageComponent;
import org.pacific_emis.surveys.wash_core.di.WashCoreComponent;

@Module
public class InteractorsModule {

    private final RemoteStorageComponent remoteStorageComponent;
    private final AssetManager assetManager;
    private final AccreditationCoreComponent accreditationCoreComponent;
    private final DataSourceComponent dataSourceComponent;
    private final WashCoreComponent washCoreComponent;

    public InteractorsModule(RemoteStorageComponent remoteStorageComponent,
                             DataSourceComponent dataSourceComponent,
                             AssetManager assetManager,
                             AccreditationCoreComponent accreditationCoreComponent,
                             WashCoreComponent washCoreComponent) {
        this.remoteStorageComponent = remoteStorageComponent;
        this.dataSourceComponent = dataSourceComponent;
        this.assetManager = assetManager;
        this.accreditationCoreComponent = accreditationCoreComponent;
        this.washCoreComponent = washCoreComponent;
    }

    @Provides
    SettingsInteractor provideSettingsInteractor(Parser<List<School>> schoolsParser,
                                                 LocalSettings localSettings) {
        return new SettingsInteractor(
                remoteStorageComponent.getRemoteStorageAccessor(),
                remoteStorageComponent.getRemoteStorage(),
                schoolsParser,
                assetManager,
                localSettings,
                new SettingsInteractor.SurveyAccessor(
                        dataSourceComponent.getDataRepository(),
                        accreditationCoreComponent.getDataSource(),
                        washCoreComponent.getDataSource(),
                        accreditationCoreComponent.getSurveyParser(),
                        washCoreComponent.getSurveyParser(),
                        assetManager
                )
        );
    }

}
