package fm.doe.national.remote_storage.di;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.core.utils.LifecycleListener;
import fm.doe.national.remote_storage.data.accessor.RemoteStorageAccessor;
import fm.doe.national.remote_storage.data.accessor.RemoteStorageAccessorImpl;
import fm.doe.national.remote_storage.data.storage.DriveRemoteStorage;
import fm.doe.national.remote_storage.data.storage.RemoteStorage;
import fm.doe.national.remote_storage.data.uploader.RemoteUploader;
import fm.doe.national.remote_storage.data.uploader.WorkerRemoteUploader;

@Module
public class RemoteStorageModule {
    @Provides
    @RemoteStorageScope
    RemoteStorage provideRemoteStorage() {
        return new DriveRemoteStorage();
    }

    @Provides
    @RemoteStorageScope
    RemoteUploader provideRemoteUploader() {
        return new WorkerRemoteUploader();
    }

    @Provides
    @RemoteStorageScope
    RemoteStorageAccessor provideRemoteStorageAccsessor(LifecycleListener lifecycleListener,
                                                        Context context,
                                                        RemoteStorage remoteStorage,
                                                        RemoteUploader uploader) {
        return new RemoteStorageAccessorImpl(lifecycleListener, context, uploader, remoteStorage);
    }
}
