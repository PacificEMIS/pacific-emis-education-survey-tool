package fm.doe.national.app_support.di;

import dagger.Component;
import fm.doe.national.app_support.di.modules.CloudModule;
import fm.doe.national.app_support.di.modules.InteractorsModule;
import fm.doe.national.core.di.CoreComponent;
import fm.doe.national.data.cloud.CloudRepository;
import fm.doe.national.data.cloud.drive.DriveCloudAccessor;
import fm.doe.national.data.cloud.drive.DriveCloudPreferences;
import fm.doe.national.data.cloud.dropbox.DropboxCloudAccessor;
import fm.doe.national.data.cloud.dropbox.DropboxCloudPreferences;
import fm.doe.national.data.cloud.uploader.CloudUploader;
import fm.doe.national.domain.SettingsInteractor;

@AppScope
@Component(modules = {
        CloudModule.class,
        InteractorsModule.class
}, dependencies = {
        CoreComponent.class
})
public interface AppComponent {

    DropboxCloudPreferences getDropboxCloudPreferences();

    DriveCloudPreferences getDriveCloudPreferences();

    DriveCloudAccessor getDriveCloudAccessor();

    DropboxCloudAccessor getDropboxCloudAccessor();

    CloudRepository getCloudRepository();

    CloudUploader getCloudUploader();

    SettingsInteractor getSettingsInteractor();

}
