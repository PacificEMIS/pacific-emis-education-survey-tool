package org.pacific_emis.surveys.remote_data.di;

import org.pacific_emis.surveys.remote_data.RemoteData;
import org.pacific_emis.surveys.remote_data.RemoteDataImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class RemoteDataModule {
    @Provides
    RemoteData provideRemoteData() {
        return new RemoteDataImpl();
    }
}
