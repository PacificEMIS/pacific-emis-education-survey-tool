package fm.doe.national.app_support.di;

import android.content.SharedPreferences;

import org.simpleframework.xml.core.Persister;

import java.util.List;

import javax.inject.Singleton;

import dagger.Component;
import fm.doe.national.app_support.di.modules.LocalDataSourceModule;
import fm.doe.national.app_support.di.modules.SerializersModule;
import fm.doe.national.app_support.di.modules.SharedPreferencesModule;
import fm.doe.national.data.cloud.CloudRepository;
import fm.doe.national.data.cloud.drive.DriveCloudAccessor;
import fm.doe.national.data.cloud.drive.DriveCloudPreferences;
import fm.doe.national.data.cloud.dropbox.DropboxCloudAccessor;
import fm.doe.national.data.cloud.dropbox.DropboxCloudPreferences;
import fm.doe.national.data.cloud.uploader.CloudUploader;
import fm.doe.national.data.data_source.DataSource;
import fm.doe.national.data.files.PicturesRepository;
import fm.doe.national.data.model.School;
import fm.doe.national.data.model.Survey;
import fm.doe.national.data.serialization.parsers.Parser;
import fm.doe.national.data.serialization.serializers.Serializer;
import fm.doe.national.app_support.di.modules.CloudModule;
import fm.doe.national.app_support.di.modules.ContextModule;
import fm.doe.national.app_support.di.modules.GsonModule;
import fm.doe.national.app_support.di.modules.InteractorsModule;
import fm.doe.national.app_support.di.modules.LifecycleModule;
import fm.doe.national.domain.SettingsInteractor;
import fm.doe.national.domain.SurveyInteractor;
import fm.doe.national.app_support.utils.LifecycleListener;

@Singleton
@Component(modules = {
        ContextModule.class,
        LocalDataSourceModule.class,
        GsonModule.class,
        CloudModule.class,
        SharedPreferencesModule.class,
        SerializersModule.class,
        LifecycleModule.class,
        InteractorsModule.class})
public interface AppComponent {
    Parser<Survey> getSchoolAccreditationParser();
    Parser<List<School>> getSchoolsParser();
    Serializer<Survey> getSchoolAccreditationSerizlizer();
    Persister getPersister();

    SharedPreferences getSharedPreferences();
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
}
