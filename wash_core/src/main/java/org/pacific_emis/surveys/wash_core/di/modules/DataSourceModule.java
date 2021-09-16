package org.pacific_emis.surveys.wash_core.di.modules;

import android.content.Context;

import org.pacific_emis.surveys.core.preferences.LocalSettings;
import org.pacific_emis.surveys.wash_core.data.data_source.RoomWashDataSource;
import org.pacific_emis.surveys.wash_core.data.data_source.WashDataSource;
import org.pacific_emis.surveys.wash_core.di.WashCoreScope;

import dagger.Module;
import dagger.Provides;

@Module
public class DataSourceModule {

    @Provides
    @WashCoreScope
    public WashDataSource provideDataSource(Context context, LocalSettings localSettings) {
        return new RoomWashDataSource(context);
    }

}
