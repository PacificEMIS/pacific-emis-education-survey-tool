package org.pacific_emis.surveys.accreditation_core.di.modules;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import org.pacific_emis.surveys.accreditation_core.data.data_source.AccreditationDataSource;
import org.pacific_emis.surveys.accreditation_core.data.data_source.RoomAccreditationDataSource;
import org.pacific_emis.surveys.accreditation_core.di.AccreditationCoreScope;
import org.pacific_emis.surveys.core.preferences.LocalSettings;

@Module
public class DataSourceModule {

    @Provides
    @AccreditationCoreScope
    public AccreditationDataSource provideDataSource(Context context, LocalSettings localSettings) {
        return new RoomAccreditationDataSource(context, localSettings);
    }

}
