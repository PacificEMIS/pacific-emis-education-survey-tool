package fm.doe.national.di.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.data_source.db.DbAccreditationDataSource;
import fm.doe.national.data_source.db.dao.DatabaseHelper;

@Module(includes = DatabaseHelperModule.class)
public class AccreditationDataSourceModule {

    @Provides
    @Singleton
    public DbAccreditationDataSource provideAccreditationDataSource(DatabaseHelper helper) {
        return new DbAccreditationDataSource(helper);
    }

}
