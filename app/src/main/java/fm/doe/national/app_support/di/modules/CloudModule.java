package fm.doe.national.app_support.di.modules;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import fm.doe.national.core.di.FeatureScope;
import fm.doe.national.data.cloud.CloudAccessor;
import fm.doe.national.data.cloud.CloudRepository;
import fm.doe.national.data.cloud.CloudType;
import fm.doe.national.data.cloud.CloudTypeKey;
import fm.doe.national.data.cloud.EmptyCloudAccessor;
import fm.doe.national.data.cloud.MultipleCloudsRepository;
import fm.doe.national.data.cloud.drive.DriveCloudAccessor;
import fm.doe.national.data.cloud.dropbox.DropboxCloudAccessor;
import fm.doe.national.data.cloud.uploader.CloudUploader;
import fm.doe.national.data.cloud.uploader.WorkerCloudUploader;

@Module
public class CloudModule {

    @Provides
    @IntoMap()
    @CloudTypeKey(CloudType.DRIVE)
    @FeatureScope
    public CloudAccessor provideDriveCloudAccessorForSet(Context context) {
        return new DriveCloudAccessor(context);
    }

    @Provides
    @IntoMap
    @CloudTypeKey(CloudType.DROPBOX)
    @FeatureScope
    public CloudAccessor provideDropboxCloudAccessorForSet(Context context) {
        return new DropboxCloudAccessor(context);
    }

    @Provides
    @IntoMap
    @CloudTypeKey(CloudType.EMPTY)
    @FeatureScope
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
    @FeatureScope
    public CloudRepository provideCloudRepository(Map<CloudType, CloudAccessor> accessors, SharedPreferences sharedPreferences) {
        return new MultipleCloudsRepository(accessors, sharedPreferences);
    }

    @Provides
    @FeatureScope
    public CloudUploader provideCloudUploader() {
        return new WorkerCloudUploader();
    }
}
