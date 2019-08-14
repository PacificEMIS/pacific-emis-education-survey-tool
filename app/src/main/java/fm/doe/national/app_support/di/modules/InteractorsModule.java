package fm.doe.national.app_support.di.modules;

import android.content.res.AssetManager;

import java.util.List;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.accreditation_core.di.AccreditationCoreComponent;
import fm.doe.national.core.data.model.School;
import fm.doe.national.core.data.serialization.Parser;
import fm.doe.national.core.preferences.LocalSettings;
import fm.doe.national.domain.SettingsInteractor;
import fm.doe.national.remote_storage.di.RemoteStorageComponent;
import fm.doe.national.wash_core.di.WashCoreComponent;

@Module
public class InteractorsModule {

    private final RemoteStorageComponent remoteStorageComponent;
    private final AssetManager assetManager;
    private final AccreditationCoreComponent accreditationCoreComponent;
    private final WashCoreComponent washCoreComponent;

    public InteractorsModule(RemoteStorageComponent remoteStorageComponent,
                             AssetManager assetManager,
                             AccreditationCoreComponent accreditationCoreComponent,
                             WashCoreComponent washCoreComponent) {
        this.remoteStorageComponent = remoteStorageComponent;
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
                        accreditationCoreComponent.getDataSource(),
                        washCoreComponent.getDataSource(),
                        accreditationCoreComponent.getSurveyParser(),
                        washCoreComponent.getSurveyParser(),
                        assetManager
                )
        );
    }

}
