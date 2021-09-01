package org.pacific_emis.surveys.core.di.modules;

import org.pacific_emis.surveys.core.data.remote_data_source.CoreRemoteDataSource;
import org.pacific_emis.surveys.core.di.CoreScope;
import org.pacific_emis.surveys.core.preferences.LocalSettings;

import dagger.Module;
import dagger.Provides;

@Module
public class RemoteDataSourceModule {

    @Provides
    @CoreScope
    public CoreRemoteDataSource provideRemoteDataSource(LocalSettings localSettings) {
        return CoreRemoteDataSource.createByAppRegion(localSettings.getAppRegion(), localSettings.getEmisApi());
    }

}
