package fm.doe.national.di;

import javax.inject.Singleton;

import dagger.Component;
import fm.doe.national.data_source.AccreditationDataSource;
import fm.doe.national.di.modules.AccreditationDataSourceModule;
import fm.doe.national.di.modules.ContextModule;
import fm.doe.national.di.modules.DatabaseHelperModule;

@Singleton
@Component(modules = {ContextModule.class, DatabaseHelperModule.class, AccreditationDataSourceModule.class})
public interface AppComponent {

    AccreditationDataSource getAccreditationDataSource();

}
