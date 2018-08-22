package fm.doe.national.di;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Component;
import fm.doe.national.data.cloud.drive.DriveActivity;
import fm.doe.national.data.cloud.drive.DriveCloudAccessor;
import fm.doe.national.di.modules.AccreditationDataSourceModule;
import fm.doe.national.di.modules.CloudModule;
import fm.doe.national.di.modules.ContextModule;
import fm.doe.national.di.modules.ConvertersModule;
import fm.doe.national.di.modules.DatabaseHelperModule;
import fm.doe.national.di.modules.GsonModule;
import fm.doe.national.ui.screens.main.MainPresenter;

@Singleton
@Component(modules = {
        ContextModule.class,
        DatabaseHelperModule.class,
        AccreditationDataSourceModule.class,
        GsonModule.class,
        ConvertersModule.class,
        CloudModule.class,
})
public interface AppComponent {

    Gson getGson();
    DriveCloudAccessor getDriveCloudAccessor();
    void inject(DriveActivity target);
    void inject(MainPresenter target);
}
