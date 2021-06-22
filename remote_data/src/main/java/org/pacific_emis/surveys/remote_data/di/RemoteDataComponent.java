package org.pacific_emis.surveys.remote_data.di;

import org.pacific_emis.surveys.remote_data.RemoteData;

import dagger.Component;

@Component(modules = {
        RemoteDataModule.class
})
public interface RemoteDataComponent {
    RemoteData getRemoteData();
}
