package fm.doe.national.app_support.di.modules;

import android.content.res.AssetManager;

import java.util.List;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.cloud.di.CloudComponent;
import fm.doe.national.core.data.model.School;
import fm.doe.national.core.data.serialization.Parser;
import fm.doe.national.core.preferences.GlobalPreferences;
import fm.doe.national.data_source_injector.di.DataSourceComponent;
import fm.doe.national.domain.SettingsInteractor;

@Module
public class InteractorsModule {

    private final CloudComponent cloudComponent;
    private final AssetManager assetManager;
    private final DataSourceComponent dataSourceComponent;

    public InteractorsModule(CloudComponent cloudComponent, AssetManager assetManager, DataSourceComponent dataSourceComponent) {
        this.cloudComponent = cloudComponent;
        this.assetManager = assetManager;
        this.dataSourceComponent = dataSourceComponent;
    }

    @Provides
    SettingsInteractor provideSettingsInteractor(Parser<List<School>> schoolsParser,
                                                 GlobalPreferences globalPreferences) {
        return new SettingsInteractor(
                cloudComponent.getCloudRepository(),
                dataSourceComponent.getDataSource(),
                dataSourceComponent.getSurveyParser(),
                schoolsParser,
                assetManager,
                globalPreferences
        );
    }

}
