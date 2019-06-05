package fm.doe.national.wash_core.di.modules;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.core.preferences.GlobalPreferences;
import fm.doe.national.wash_core.data.data_source.RoomWashDataSource;
import fm.doe.national.wash_core.data.data_source.WashDataSource;
import fm.doe.national.wash_core.di.WashCoreScope;

@Module
public class DataSourceModule {

    @Provides
    @WashCoreScope
    public WashDataSource provideDataSource(Context context, GlobalPreferences globalPreferences) {
        return new RoomWashDataSource(context, globalPreferences);
    }

}
