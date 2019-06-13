package fm.doe.national.app_support.di.modules;

import android.content.res.AssetManager;

import java.util.List;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.accreditation_core.di.AccreditationCoreComponent;
import fm.doe.national.cloud.di.CloudComponent;
import fm.doe.national.core.data.model.School;
import fm.doe.national.core.data.serialization.Parser;
import fm.doe.national.core.preferences.GlobalPreferences;
import fm.doe.national.domain.SettingsInteractor;
import fm.doe.national.wash_core.di.WashCoreComponent;

@Module
public class InteractorsModule {

    private final CloudComponent cloudComponent;
    private final AssetManager assetManager;
    private final AccreditationCoreComponent accreditationCoreComponent;
    private final WashCoreComponent washCoreComponent;

    public InteractorsModule(CloudComponent cloudComponent,
                             AssetManager assetManager,
                             AccreditationCoreComponent accreditationCoreComponent,
                             WashCoreComponent washCoreComponent) {
        this.cloudComponent = cloudComponent;
        this.assetManager = assetManager;
        this.accreditationCoreComponent = accreditationCoreComponent;
        this.washCoreComponent = washCoreComponent;
    }

    @Provides
    SettingsInteractor provideSettingsInteractor(Parser<List<School>> schoolsParser,
                                                 GlobalPreferences globalPreferences) {
        return new SettingsInteractor(
                cloudComponent.getCloudRepository(),
                schoolsParser,
                assetManager,
                globalPreferences,
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
