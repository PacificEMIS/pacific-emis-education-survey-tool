package org.pacific_emis.surveys.accreditation_core.di.modules;

import android.content.Context;

import org.pacific_emis.surveys.accreditation_core.data.data_source.AccreditationDataSource;
import org.pacific_emis.surveys.accreditation_core.data.data_source.AccreditationLocalDataSource;
import org.pacific_emis.surveys.accreditation_core.di.AccreditationCoreScope;
import org.pacific_emis.surveys.core.preferences.LocalSettings;

import dagger.Module;
import dagger.Provides;

@Module
public class DataSourceModule {

    @Provides
    @AccreditationCoreScope
    public AccreditationDataSource provideDataSource(Context context, LocalSettings localSettings) {
        return new AccreditationLocalDataSource(context, localSettings);
    }

}
