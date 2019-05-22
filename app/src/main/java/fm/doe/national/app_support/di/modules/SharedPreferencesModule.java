package fm.doe.national.app_support.di.modules;

import android.content.Context;
import android.content.SharedPreferences;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.core.di.FeatureScope;
import fm.doe.national.data.cloud.drive.DriveCloudPreferences;
import fm.doe.national.data.cloud.dropbox.DropboxCloudPreferences;

import static android.content.Context.MODE_PRIVATE;

@Module
public class SharedPreferencesModule {
    private static final String NAME_APP_PREFS_GLOBAL = "APP_PREFS_GLOBAL";

    @Provides
    @FeatureScope
    public SharedPreferences provideSharedPreferences(Context context) {
        return context.getSharedPreferences(NAME_APP_PREFS_GLOBAL, MODE_PRIVATE);
    }

    @Provides
    @FeatureScope
    public DropboxCloudPreferences provideDropboxCloudPreferences(SharedPreferences sp) {
        return new DropboxCloudPreferences(sp);
    }

    @Provides
    @FeatureScope
    public DriveCloudPreferences provideDriveCloudPreferences(SharedPreferences sp) {
        return new DriveCloudPreferences(sp);
    }

}