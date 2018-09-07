package fm.doe.national.di.modules;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.data.data_source.DataSource;
import fm.doe.national.data.data_source.db.OrmLiteDataSource;
import fm.doe.national.data.data_source.db.dao.DatabaseHelper;
import fm.doe.national.data.files.FileRepository;

@Module
public class LocalDataSourceModule {

    @Provides
    @Singleton
    public DataSource provideAccreditationDataSource(DatabaseHelper helper) {
        return new OrmLiteDataSource(helper);
    }

    @Provides
    @Singleton
    public FileRepository provideFileRepository(Context context) {
        return new FileRepository(context);
    }

}
