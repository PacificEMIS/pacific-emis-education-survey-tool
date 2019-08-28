package fm.doe.national.remote_settings.di;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.core.preferences.LocalSettings;
import fm.doe.national.remote_settings.model.RemoteSettings;
import fm.doe.national.remote_storage.di.RemoteStorageComponent;

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
