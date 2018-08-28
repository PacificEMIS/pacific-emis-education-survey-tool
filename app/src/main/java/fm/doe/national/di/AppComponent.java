package fm.doe.national.di;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Component;
import fm.doe.national.data.cloud.drive.DriveCloudAccessor;
import fm.doe.national.data.cloud.drive.DriveCloudPreferences;
import fm.doe.national.data.cloud.dropbox.DropboxCloudAccessor;
import fm.doe.national.data.cloud.dropbox.DropboxCloudPreferences;
import fm.doe.national.di.modules.AccreditationDataSourceModule;
import fm.doe.national.di.modules.CloudModule;
import fm.doe.national.di.modules.ContextModule;
import fm.doe.national.di.modules.DatabaseHelperModule;
import fm.doe.national.di.modules.GsonModule;
import fm.doe.national.di.modules.ParsersModule;
import fm.doe.national.di.modules.SharedPreferencesModule;
import fm.doe.national.ui.screens.cloud.DriveActivity;
import fm.doe.national.ui.screens.main.MainPresenter;
import fm.doe.national.ui.screens.standard.StandardPresenter;

@Singleton
@Component(modules = {
        ContextModule.class,
        DatabaseHelperModule.class,
        AccreditationDataSourceModule.class,
        GsonModule.class,
        CloudModule.class,
        SharedPreferencesModule.class,
        ParsersModule.class})
public interface AppComponent {
    Gson getGson();
    SharedPreferences getSharedPreferences();
    DropboxCloudPreferences getDropboxCloudPreferences();
    DriveCloudPreferences getDriveCloudPreferences();
    DriveCloudAccessor getDriveCloudAccessor();
    DropboxCloudAccessor getDropboxCloudAccessor();
    void inject(DriveActivity target);
    void inject(MainPresenter target);
    void inject(StandardPresenter standardPresenter);
}
