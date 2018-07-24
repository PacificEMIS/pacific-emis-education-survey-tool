package fm.doe.national.di;

import javax.inject.Singleton;

import dagger.Component;
import fm.doe.national.data.data_source.DataSource;
import fm.doe.national.data.data_source.static_source.StaticDataSource;
import fm.doe.national.di.modules.AccreditationDataSourceModule;
import fm.doe.national.di.modules.ContextModule;
import fm.doe.national.di.modules.ConvertersModule;
import fm.doe.national.di.modules.DatabaseHelperModule;
import fm.doe.national.di.modules.GsonModule;
import fm.doe.national.di.modules.StaticDataSourceModule;

@Singleton
@Component(modules = {ContextModule.class,
        DatabaseHelperModule.class,
        AccreditationDataSourceModule.class,
        GsonModule.class,
        StaticDataSourceModule.class,
        ConvertersModule.class})
public interface AppComponent {

    DataSource getAccreditationDataSource();

    StaticDataSource getStaticDataSource();

}
