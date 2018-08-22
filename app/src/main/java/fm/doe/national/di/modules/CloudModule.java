package fm.doe.national.di.modules;

import android.content.Context;

import java.util.Map;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import fm.doe.national.data.cloud.CloudAccessor;
import fm.doe.national.data.cloud.CloudRepository;
import fm.doe.national.data.cloud.Clouds;
import fm.doe.national.data.cloud.CloudTypeKey;
import fm.doe.national.data.cloud.drive.DriveCloudAccessor;
import fm.doe.national.data.cloud.dropbox.DropboxCloudAccessor;

@Module
public class CloudModule {

    @Provides
    @IntoMap()
    @CloudTypeKey(CloudAccessor.Type.DRIVE)
    @Singleton
    public CloudAccessor provideDriveCloudAccessorForSet(Context context) {
        return new DriveCloudAccessor(context);
    }

    @Provides
    @IntoMap
    @CloudTypeKey(CloudAccessor.Type.DROPBOX)
    @Singleton
    public CloudAccessor provideDropboxCloudAccessorForSet(Context context) {
        return new DropboxCloudAccessor(context);
    }


    @Provides
    public DriveCloudAccessor provideDriveCloudAccessor(Map<CloudAccessor.Type, CloudAccessor> cloudAccessorMap) {
        return (DriveCloudAccessor) cloudAccessorMap.get(CloudAccessor.Type.DRIVE);
    }

    @Provides
    public DropboxCloudAccessor provideDropboxCloudAccessor(Map<CloudAccessor.Type, CloudAccessor> cloudAccessorMap) {
        return (DropboxCloudAccessor) cloudAccessorMap.get(CloudAccessor.Type.DROPBOX);
    }

    @Provides
    @Singleton
    public CloudRepository provideCloudRepository(Map<CloudAccessor.Type, CloudAccessor> accessors) {
        return new Clouds(accessors);
    }
}
