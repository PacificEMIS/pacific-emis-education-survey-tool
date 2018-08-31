package fm.doe.national.di.modules;

import android.content.Context;

import java.util.Map;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import fm.doe.national.data.cloud.CloudAccessor;
import fm.doe.national.data.cloud.CloudRepository;
import fm.doe.national.data.cloud.CloudType;
import fm.doe.national.data.cloud.MultipleCloudsRepository;
import fm.doe.national.data.cloud.CloudTypeKey;
import fm.doe.national.data.cloud.drive.DriveCloudAccessor;
import fm.doe.national.data.cloud.dropbox.DropboxCloudAccessor;

@Module
public class CloudModule {

    @Provides
    @IntoMap()
    @CloudTypeKey(CloudType.DRIVE)
    @Singleton
    public CloudAccessor provideDriveCloudAccessorForSet(Context context) {
        return new DriveCloudAccessor(context);
    }

    @Provides
    @IntoMap
    @CloudTypeKey(CloudType.DROPBOX)
    @Singleton
    public CloudAccessor provideDropboxCloudAccessorForSet(Context context) {
        return new DropboxCloudAccessor(context);
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
    @Singleton
    public CloudRepository provideCloudRepository(Map<CloudType, CloudAccessor> accessors) {
        return new MultipleCloudsRepository(accessors);
    }
}
