package fm.doe.national.di;

import android.content.SharedPreferences;

import org.simpleframework.xml.core.Persister;

import java.util.List;

import javax.inject.Singleton;

import dagger.Component;
import fm.doe.national.data.cloud.CloudRepository;
import fm.doe.national.data.cloud.drive.DriveCloudAccessor;
import fm.doe.national.data.cloud.drive.DriveCloudPreferences;
import fm.doe.national.data.cloud.dropbox.DropboxCloudAccessor;
import fm.doe.national.data.cloud.dropbox.DropboxCloudPreferences;
import fm.doe.national.data.cloud.uploader.CloudUploader;
import fm.doe.national.data.data_source.DataSource;
import fm.doe.national.data.data_source.models.School;
import fm.doe.national.data.data_source.models.serializable.LinkedSchoolAccreditation;
import fm.doe.national.data.files.FileRepository;
import fm.doe.national.data.parsers.Parser;
import fm.doe.national.data.serializers.Serializer;
import fm.doe.national.di.modules.LocalDataSourceModule;
import fm.doe.national.di.modules.CloudModule;
import fm.doe.national.di.modules.ContextModule;
import fm.doe.national.di.modules.DatabaseHelperModule;
import fm.doe.national.di.modules.GsonModule;
import fm.doe.national.di.modules.InteractorsModule;
import fm.doe.national.di.modules.LifecycleModule;
import fm.doe.national.di.modules.ParsersModule;
import fm.doe.national.di.modules.SerializersModule;
import fm.doe.national.di.modules.SharedPreferencesModule;
import fm.doe.national.domain.SettingsInteractor;
import fm.doe.national.utils.LifecycleListener;

@Singleton
@Component(modules = {
        ContextModule.class,
        DatabaseHelperModule.class,
        LocalDataSourceModule.class,
        GsonModule.class,
        CloudModule.class,
        SharedPreferencesModule.class,
        ParsersModule.class,
        SerializersModule.class,
        LifecycleModule.class,
        InteractorsModule.class})
public interface AppComponent {
    Parser<LinkedSchoolAccreditation> getSchoolAccreditationParser();
    Parser<List<School>> getSchoolsParser();
    Serializer<LinkedSchoolAccreditation> getSchoolAccreditationSerizlizer();
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
    FileRepository getFileRepository();

    SettingsInteractor getSettingsInteractor();
}
