package fm.doe.national.accreditation_core.di.modules;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.accreditation_core.data.data_source.AccreditationDataSource;
import fm.doe.national.accreditation_core.data.data_source.RoomAccreditationDataSource;
import fm.doe.national.accreditation_core.di.AccreditationCoreScope;
import fm.doe.national.core.preferences.LocalSettings;

@Module
public class DataSourceModule {

    @Provides
    @AccreditationCoreScope
    public AccreditationDataSource provideDataSource(Context context, LocalSettings localSettings) {
        return new RoomAccreditationDataSource(context, localSettings);
    }

}
