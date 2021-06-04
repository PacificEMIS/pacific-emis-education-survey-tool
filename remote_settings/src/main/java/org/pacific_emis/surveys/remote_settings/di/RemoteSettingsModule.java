package org.pacific_emis.surveys.remote_settings.di;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import org.pacific_emis.surveys.core.preferences.LocalSettings;
import org.pacific_emis.surveys.remote_settings.model.RemoteSettings;
import org.pacific_emis.surveys.remote_storage.di.RemoteStorageComponent;

@Module
public class RemoteSettingsModule {

    private final RemoteStorageComponent remoteStorageComponent;

    public RemoteSettingsModule(RemoteStorageComponent remoteStorageComponent) {
        this.remoteStorageComponent = remoteStorageComponent;
    }

    @Provides
    @RemoteSettingsScope
    RemoteSettings provideRemoteSettings(Context appContext,  LocalSettings localSettings) {
        return new RemoteSettings(appContext, localSettings, remoteStorageComponent.getRemoteStorage());
    }

}
