package fm.doe.national.di;

import com.google.gson.Gson;
import com.tickaroo.tikxml.TikXml;

import javax.inject.Singleton;

import dagger.Component;
import fm.doe.national.di.modules.AccreditationDataSourceModule;
import fm.doe.national.di.modules.ContextModule;
import fm.doe.national.di.modules.DatabaseHelperModule;
import fm.doe.national.di.modules.GsonModule;
import fm.doe.national.di.modules.ParsersModule;
import fm.doe.national.ui.screens.main.MainActivity;

@Singleton
@Component(modules = {ContextModule.class,
        DatabaseHelperModule.class,
        AccreditationDataSourceModule.class,
        GsonModule.class,
        ParsersModule.class})
public interface AppComponent {

    Gson getGson();
    void inject(MainActivity mainActivity);
}
