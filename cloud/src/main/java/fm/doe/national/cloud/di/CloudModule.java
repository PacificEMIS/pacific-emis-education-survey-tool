package fm.doe.national.cloud.di;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import fm.doe.national.cloud.model.CloudAccessor;
import fm.doe.national.cloud.model.CloudRepository;
import fm.doe.national.cloud.model.EmptyCloudAccessor;
import fm.doe.national.cloud.model.MultipleCloudsRepository;
import fm.doe.national.cloud.model.drive.DriveCloudAccessor;
import fm.doe.national.cloud.model.drive.DriveCloudPreferences;
import fm.doe.national.cloud.model.dropbox.DropboxCloudAccessor;
import fm.doe.national.cloud.model.dropbox.DropboxCloudPreferences;
import fm.doe.national.cloud.model.uploader.CloudUploader;
import fm.doe.national.cloud.model.uploader.WorkerCloudUploader;
import fm.doe.national.core.data.model.CloudType;
import fm.doe.national.core.utils.LifecycleListener;

@Module
public class CloudModule {

    @Provides
    @IntoMap()
    @CloudTypeKey(CloudType.DRIVE)
    @CloudScope
    public CloudAccessor provideDriveCloudAccessorForSet(Context context,
                                                         DriveCloudPreferences cloudPreferences,
                                                         LifecycleListener lifecycleListener) {
        return new DriveCloudAccessor(context, cloudPreferences, lifecycleListener);
    }

    @Provides
    @IntoMap
    @CloudTypeKey(CloudType.DROPBOX)
    @CloudScope
    public CloudAccessor provideDropboxCloudAccessorForSet(Context context,
                                                           DropboxCloudPreferences cloudPreferences,
                                                           LifecycleListener lifecycleListener) {
        return new DropboxCloudAccessor(context, cloudPreferences, lifecycleListener);
    }

    @Provides
    @IntoMap
    @CloudTypeKey(CloudType.EMPTY)
    @CloudScope
    public CloudAccessor provideEmptyCloudAccessorForSet() {
        return new EmptyCloudAccessor();
    }


    @Provides
    public DriveCloudAccessor provideDriveCloudAccessor(Map<CloudType, CloudAccessor> cloudAccessorMap) {
        return (DriveCloudAccessor) cloudAccessorMap.get(CloudType.DRIVE);
    }

    @Provides
    public DropboxCloudAccessor provideDropboxCloudAccessor(Map<CloudType, CloudAccessor> cloudAccessorMap) {
        return (DropboxCloudAccessor) cloudAccessorMap.get(CloudType.DROPBOX);
    }

    @Provides
    @CloudScope
    public CloudRepository provideCloudRepository(Map<CloudType, CloudAccessor> accessors, SharedPreferences sharedPreferences) {
        return new MultipleCloudsRepository(accessors, sharedPreferences);
    }

    @Provides
    @CloudScope
    public CloudUploader provideCloudUploader() {
        return new WorkerCloudUploader();
    }


    @Provides
    @CloudScope
    public DropboxCloudPreferences provideDropboxCloudPreferences(SharedPreferences sp) {
        return new DropboxCloudPreferences(sp);
    }

    @Provides
    @CloudScope
    public DriveCloudPreferences provideDriveCloudPreferences(SharedPreferences sp) {
        return new DriveCloudPreferences(sp);
    }

}
