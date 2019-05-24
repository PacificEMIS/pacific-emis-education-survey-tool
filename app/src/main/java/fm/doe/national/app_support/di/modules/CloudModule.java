package fm.doe.national.app_support.di.modules;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import fm.doe.national.app_support.di.AppScope;
import fm.doe.national.core.utils.LifecycleListener;
import fm.doe.national.data.cloud.CloudAccessor;
import fm.doe.national.data.cloud.CloudRepository;
import fm.doe.national.data.cloud.CloudType;
import fm.doe.national.data.cloud.CloudTypeKey;
import fm.doe.national.data.cloud.EmptyCloudAccessor;
import fm.doe.national.data.cloud.MultipleCloudsRepository;
import fm.doe.national.data.cloud.drive.DriveCloudAccessor;
import fm.doe.national.data.cloud.drive.DriveCloudPreferences;
import fm.doe.national.data.cloud.dropbox.DropboxCloudAccessor;
import fm.doe.national.data.cloud.dropbox.DropboxCloudPreferences;
import fm.doe.national.data.cloud.uploader.CloudUploader;
import fm.doe.national.data.cloud.uploader.WorkerCloudUploader;

@Module
public class CloudModule {

    @Provides
    @IntoMap()
    @CloudTypeKey(CloudType.DRIVE)
    @AppScope
    public CloudAccessor provideDriveCloudAccessorForSet(Context context,
                                                         DriveCloudPreferences cloudPreferences,
                                                         LifecycleListener lifecycleListener) {
        return new DriveCloudAccessor(context, cloudPreferences, lifecycleListener);
    }

    @Provides
    @IntoMap
    @CloudTypeKey(CloudType.DROPBOX)
    @AppScope
    public CloudAccessor provideDropboxCloudAccessorForSet(Context context,
                                                           DropboxCloudPreferences cloudPreferences,
                                                           LifecycleListener lifecycleListener) {
        return new DropboxCloudAccessor(context, cloudPreferences, lifecycleListener);
    }

    @Provides
    @IntoMap
    @CloudTypeKey(CloudType.EMPTY)
    @AppScope
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
    @AppScope
    public CloudRepository provideCloudRepository(Map<CloudType, CloudAccessor> accessors, SharedPreferences sharedPreferences) {
        return new MultipleCloudsRepository(accessors, sharedPreferences);
    }

    @Provides
    @AppScope
    public CloudUploader provideCloudUploader() {
        return new WorkerCloudUploader();
    }


    @Provides
    @AppScope
    public DropboxCloudPreferences provideDropboxCloudPreferences(SharedPreferences sp) {
        return new DropboxCloudPreferences(sp);
    }

    @Provides
    @AppScope
    public DriveCloudPreferences provideDriveCloudPreferences(SharedPreferences sp) {
        return new DriveCloudPreferences(sp);
    }

}
