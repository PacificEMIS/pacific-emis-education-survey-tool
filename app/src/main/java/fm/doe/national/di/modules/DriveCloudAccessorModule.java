package fm.doe.national.di.modules;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.data.cloud.drive.DriveCloudAccessor;

@Module(includes = ContextModule.class)
public class DriveCloudAccessorModule {

    @Provides
    @Singleton
    public DriveCloudAccessor provideDriveCloudAccessor(Context context) {
        return new DriveCloudAccessor(context);
    }
}
