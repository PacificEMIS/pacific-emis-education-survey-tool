package com.micronesia.di.modules;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.micronesia.data_source.db.dao.DatabaseHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = ContextModule.class)
public class DatabaseHelperModule {

    @Provides
    @Singleton
    public DatabaseHelper provideDatabaseHelper(Context context) {
        return OpenHelperManager.getHelper(context, DatabaseHelper.class);
    }

}
