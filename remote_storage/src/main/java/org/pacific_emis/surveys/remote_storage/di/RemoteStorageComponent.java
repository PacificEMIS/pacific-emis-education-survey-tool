package org.pacific_emis.surveys.remote_storage.di;

import dagger.Component;
import org.pacific_emis.surveys.core.di.CoreComponent;
import org.pacific_emis.surveys.remote_storage.data.accessor.RemoteStorageAccessor;
import org.pacific_emis.surveys.remote_storage.data.storage.RemoteStorage;
import org.pacific_emis.surveys.remote_storage.data.uploader.RemoteUploader;

@RemoteStorageScope
@Component(modules = {
        RemoteStorageModule.class
}, dependencies = {
        CoreComponent.class
})
public interface RemoteStorageComponent {
    RemoteStorage getRemoteStorage();

    RemoteUploader getRemoteUploader();

    RemoteStorageAccessor getRemoteStorageAccessor();
}
