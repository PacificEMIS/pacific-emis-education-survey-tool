package fm.doe.national.remote_settings.di;

import dagger.Component;
import fm.doe.national.core.di.CoreComponent;
import fm.doe.national.remote_settings.model.RemoteSettings;
import fm.doe.national.remote_storage.data.accessor.RemoteStorageAccessor;
import fm.doe.national.remote_storage.data.storage.RemoteStorage;
import fm.doe.national.remote_storage.data.uploader.RemoteUploader;

@RemoteSettingsScope
@Component(modules = {
        RemoteSettingsModule.class
}, dependencies = {
        CoreComponent.class
})
public interface RemoteSettingsComponent {
    RemoteSettings getRemoteSettings();
}
