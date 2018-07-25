package fm.doe.national.di;

import javax.inject.Singleton;

import dagger.Component;
import fm.doe.national.di.modules.AccreditationDataSourceModule;
import fm.doe.national.di.modules.ContextModule;
import fm.doe.national.di.modules.ConvertersModule;
import fm.doe.national.di.modules.DatabaseHelperModule;
import fm.doe.national.di.modules.GsonModule;
import fm.doe.national.ui.screens.main.MainActivity;

@Singleton
@Component(modules = {ContextModule.class,
        DatabaseHelperModule.class,
        AccreditationDataSourceModule.class,
        GsonModule.class,
        ConvertersModule.class})
public interface AppComponent {

    void inject(MainActivity mainActivity);
}
