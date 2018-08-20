package fm.doe.national.di.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.data.cloud.accessors.CloudAccessor;
import fm.doe.national.data.cloud.accessors.DriveCloudAccessor;

@Module
public class DriveCloudAccessorModule {

    @Provides
    @Singleton
    public CloudAccessor provideDriveCloudAccessor() {
        return new DriveCloudAccessor();
    }
}
