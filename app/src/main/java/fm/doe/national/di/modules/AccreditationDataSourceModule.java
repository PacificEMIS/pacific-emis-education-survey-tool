package fm.doe.national.di.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.data.data_source.db.OrmLiteDataSource;
import fm.doe.national.data.data_source.db.dao.DatabaseHelper;

@Module(includes = DatabaseHelperModule.class)
public class AccreditationDataSourceModule {

    @Provides
    @Singleton
    public OrmLiteDataSource provideAccreditationDataSource(DatabaseHelper helper) {
        return new OrmLiteDataSource(helper);
    }

}
