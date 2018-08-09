package fm.doe.national.di.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.data.data_source.db.DatabaseDataSource;
import fm.doe.national.data.data_source.db.dao.DatabaseHelper;

@Module(includes = DatabaseHelperModule.class)
public class AccreditationDataSourceModule {

    @Provides
    @Singleton
    public DatabaseDataSource provideAccreditationDataSource(DatabaseHelper helper) {
        return new DatabaseDataSource(helper);
    }

}
