package fm.doe.national.app_support.di.modules;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.data.cloud.drive.DriveCloudPreferences;
import fm.doe.national.data.cloud.dropbox.DropboxCloudPreferences;

import static android.content.Context.MODE_PRIVATE;

@Module
public class SharedPreferencesModule {
    private static final String NAME_APP_PREFS_GLOBAL = "APP_PREFS_GLOBAL";

    @Provides
    @Singleton
    public SharedPreferences provideSharedPreferences(Context context) {
        return context.getSharedPreferences(NAME_APP_PREFS_GLOBAL, MODE_PRIVATE);
    }

    // TODO: use @Named injection
    @Provides
    @Singleton
    public DropboxCloudPreferences provideDropboxCloudPreferences() {
        return new DropboxCloudPreferences();
    }

    @Provides
    @Singleton
    public DriveCloudPreferences provideDriveCloudPreferences() {
        return new DriveCloudPreferences();
    }

}