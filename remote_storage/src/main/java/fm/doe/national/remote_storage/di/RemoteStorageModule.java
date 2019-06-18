package fm.doe.national.remote_storage.di;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.remote_storage.data.storage.FirebaseRemoteStorage;
import fm.doe.national.remote_storage.data.storage.RemoteStorage;
import fm.doe.national.remote_storage.data.uploader.RemoteUploader;
import fm.doe.national.remote_storage.data.uploader.WorkerRemoteUploader;

@Module
public class RemoteStorageModule {
    @Provides
    @RemoteStorageScope
    RemoteStorage provideRemoteStorage() {
        return new FirebaseRemoteStorage();
    }

    @Provides
    @RemoteStorageScope
    RemoteUploader provideRemoteUploader() {
        return new WorkerRemoteUploader();
    }
}
