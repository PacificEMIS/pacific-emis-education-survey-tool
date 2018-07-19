package com.micronesia.di;


import com.micronesia.data_source.AccreditationDataSource;
import com.micronesia.data_source.db.dao.DatabaseHelper;
import com.micronesia.di.modules.AccreditationDataSourceModule;
import com.micronesia.di.modules.ContextModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ContextModule.class, DatabaseHelper.class, AccreditationDataSourceModule.class})
public interface AppComponent {

    AccreditationDataSource getAccreditationDataSource();

}
