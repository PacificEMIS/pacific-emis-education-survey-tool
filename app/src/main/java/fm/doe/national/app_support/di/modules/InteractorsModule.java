package fm.doe.national.app_support.di.modules;

import android.content.res.AssetManager;

import java.util.List;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.cloud.di.CloudComponent;
import fm.doe.national.core.data.data_source.DataSource;
import fm.doe.national.core.data.model.School;
import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.data.serialization.parsers.Parser;
import fm.doe.national.core.preferences.GlobalPreferences;
import fm.doe.national.domain.SettingsInteractor;

@Module
public class InteractorsModule {

    private final CloudComponent cloudComponent;
    private final AssetManager assetManager;

    public InteractorsModule(CloudComponent cloudComponent, AssetManager assetManager) {
        this.cloudComponent = cloudComponent;
        this.assetManager = assetManager;
    }

    @Provides
    SettingsInteractor provideSettingsInteractor(DataSource localDataRepository,
                                                 Parser<Survey> surveyParser,
                                                 Parser<List<School>> schoolsParser,
                                                 GlobalPreferences globalPreferences) {
        return new SettingsInteractor(
                cloudComponent.getCloudRepository(),
                localDataRepository,
                surveyParser,
                schoolsParser,
                assetManager,
                globalPreferences
        );
    }

}
