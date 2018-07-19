package com.micronesia.di.modules;

import com.micronesia.data_source.AccreditationDataSource;
import com.micronesia.data_source.db.OrmLiteAccreditationDataSource;
import com.micronesia.data_source.db.dao.DatabaseHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = DatabaseHelperModule.class)
public class AccreditationDataSourceModule {

    @Provides
    @Singleton
    public AccreditationDataSource provideAccreditationDataSource(DatabaseHelper helper) {
        return new OrmLiteAccreditationDataSource(helper);
    }

}
