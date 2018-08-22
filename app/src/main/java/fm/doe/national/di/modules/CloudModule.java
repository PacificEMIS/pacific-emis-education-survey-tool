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
import fm.doe.national.data.cloud.CloutTypeKey;
import fm.doe.national.data.cloud.drive.DriveCloudAccessor;
import fm.doe.national.data.cloud.dropbox.DropboxCloudAccessor;

@Module()
public class CloudModule {

    @Provides
    @IntoMap()
    @CloutTypeKey(CloudAccessor.Type.DRIVE)
    public CloudAccessor provideDriveCloudAccessorForSet(Context context) {
        return new DriveCloudAccessor(context);
    }

    @Provides
    @IntoMap
    @CloutTypeKey(CloudAccessor.Type.DROPBOX)
    public CloudAccessor provideDropboxCloudAccessorForSet(Context context) {
        return new DropboxCloudAccessor(context);
    }


    @Provides
    public DriveCloudAccessor provideDriveCloudAccessor(Map<CloudAccessor.Type, CloudAccessor> cloudAccessorMap) {
        return (DriveCloudAccessor) cloudAccessorMap.get(CloudAccessor.Type.DRIVE);
    }

    @Provides
    @Singleton
    public CloudRepository provideCloudRepository(Map<CloudAccessor.Type, CloudAccessor> accessors) {
        return new Clouds(accessors);
    }
}
