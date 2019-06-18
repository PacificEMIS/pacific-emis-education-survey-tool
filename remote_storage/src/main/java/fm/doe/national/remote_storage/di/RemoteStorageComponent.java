package fm.doe.national.remote_storage.di;

import dagger.Component;
import fm.doe.national.core.di.CoreComponent;
import fm.doe.national.remote_storage.data.storage.RemoteStorage;
import fm.doe.national.remote_storage.data.uploader.RemoteUploader;

@RemoteStorageScope
@Component(modules = {
        RemoteStorageModule.class
}, dependencies = {
        CoreComponent.class
})
public interface RemoteStorageComponent {
    RemoteStorage getRemoteStorage();

    RemoteUploader getRemoteUploader();
}
