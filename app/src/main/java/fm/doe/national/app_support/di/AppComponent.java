package fm.doe.national.app_support.di;

import org.simpleframework.xml.core.Persister;

import java.util.List;

import dagger.Component;
import fm.doe.national.app_support.di.modules.CloudModule;
import fm.doe.national.app_support.di.modules.ContextModule;
import fm.doe.national.app_support.di.modules.InteractorsModule;
import fm.doe.national.app_support.di.modules.LifecycleModule;
import fm.doe.national.app_support.di.modules.LocalDataSourceModule;
import fm.doe.national.app_support.di.modules.SerializersModule;
import fm.doe.national.core.utils.LifecycleListener;
import fm.doe.national.core.data.data_source.DataSource;
import fm.doe.national.core.data.model.School;
import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.di.CoreComponent;
import fm.doe.national.core.di.FeatureScope;
import fm.doe.national.core.interactors.SurveyInteractor;
import fm.doe.national.core.preferences.GlobalPreferences;
import fm.doe.national.data.cloud.CloudRepository;
import fm.doe.national.data.cloud.drive.DriveCloudAccessor;
import fm.doe.national.data.cloud.drive.DriveCloudPreferences;
import fm.doe.national.data.cloud.dropbox.DropboxCloudAccessor;
import fm.doe.national.data.cloud.dropbox.DropboxCloudPreferences;
import fm.doe.national.data.cloud.uploader.CloudUploader;
import fm.doe.national.data.files.PicturesRepository;
import fm.doe.national.data.serialization.parsers.Parser;
import fm.doe.national.data.serialization.serializers.Serializer;
import fm.doe.national.domain.ReportInteractor;
import fm.doe.national.domain.SettingsInteractor;

@FeatureScope
@Component(modules = {
        ContextModule.class,
        LocalDataSourceModule.class,
        CloudModule.class,
        SerializersModule.class,
        LifecycleModule.class,
        InteractorsModule.class
}, dependencies = {
        CoreComponent.class
})
public interface AppComponent {
    Parser<Survey> getSurveyParser();
    Parser<List<School>> getSchoolsParser();
    Serializer<Survey> getSurveySerializer();
    Persister getPersister();

    DropboxCloudPreferences getDropboxCloudPreferences();
    DriveCloudPreferences getDriveCloudPreferences();

    DriveCloudAccessor getDriveCloudAccessor();
    DropboxCloudAccessor getDropboxCloudAccessor();

    LifecycleListener getLifecycleListener();

    CloudRepository getCloudRepository();
    DataSource getDataSource();
    CloudUploader getCloudUploader();
    PicturesRepository getPicturesRepository();

    SettingsInteractor getSettingsInteractor();
    SurveyInteractor getSurveyInteractor();
    ReportInteractor getReportInteractor();

    GlobalPreferences getGlobalPreferences();
}
