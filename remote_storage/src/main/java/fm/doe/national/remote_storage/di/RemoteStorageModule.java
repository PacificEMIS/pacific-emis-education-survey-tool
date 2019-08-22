package fm.doe.national.remote_storage.di;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.core.data.files.FilesRepository;
import fm.doe.national.core.preferences.LocalSettings;
import fm.doe.national.core.utils.LifecycleListener;
import fm.doe.national.data_source_injector.di.DataSourceComponent;
import fm.doe.national.remote_storage.data.accessor.RemoteStorageAccessor;
import fm.doe.national.remote_storage.data.accessor.RemoteStorageAccessorImpl;
import fm.doe.national.remote_storage.data.storage.DriveRemoteStorage;
import fm.doe.national.remote_storage.data.storage.RemoteStorage;
import fm.doe.national.remote_storage.data.uploader.RemoteUploader;
import fm.doe.national.remote_storage.data.uploader.WorkerRemoteUploader;

@Module
public class RemoteStorageModule {

    private final DataSourceComponent dataSourceComponent;

    public RemoteStorageModule(DataSourceComponent dataSourceComponent) {
        this.dataSourceComponent = dataSourceComponent;
    }

    @Provides
    @RemoteStorageScope
    RemoteStorage provideRemoteStorage(Context context, LocalSettings localSettings, FilesRepository filesRepository) {
        return new DriveRemoteStorage(context, localSettings, dataSourceComponent, filesRepository);
    }

    @Provides
    @RemoteStorageScope
    RemoteUploader provideRemoteUploader() {
        return new WorkerRemoteUploader();
    }

    @Provides
    @RemoteStorageScope
    RemoteStorageAccessor provideRemoteStorageAccessor(LifecycleListener lifecycleListener,
                                                       RemoteUploader uploader,
                                                       RemoteStorage storage) {
        return new RemoteStorageAccessorImpl(lifecycleListener, uploader, storage);
    }
}
