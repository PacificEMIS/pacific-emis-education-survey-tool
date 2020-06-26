package org.pacific_emis.surveys.remote_storage.di;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import org.pacific_emis.surveys.core.data.files.FilesRepository;
import org.pacific_emis.surveys.core.preferences.LocalSettings;
import org.pacific_emis.surveys.core.utils.LifecycleListener;
import org.pacific_emis.surveys.data_source_injector.di.DataSourceComponent;
import org.pacific_emis.surveys.remote_storage.data.accessor.RemoteStorageAccessor;
import org.pacific_emis.surveys.remote_storage.data.accessor.RemoteStorageAccessorImpl;
import org.pacific_emis.surveys.remote_storage.data.storage.DriveRemoteStorage;
import org.pacific_emis.surveys.remote_storage.data.storage.RemoteStorage;
import org.pacific_emis.surveys.remote_storage.data.uploader.RemoteUploader;
import org.pacific_emis.surveys.remote_storage.data.uploader.WorkerRemoteUploader;

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
    RemoteUploader provideRemoteUploader(Context context) {
        return new WorkerRemoteUploader(context);
    }

    @Provides
    @RemoteStorageScope
    RemoteStorageAccessor provideRemoteStorageAccessor(LifecycleListener lifecycleListener,
                                                       RemoteUploader uploader,
                                                       RemoteStorage storage) {
        return new RemoteStorageAccessorImpl(lifecycleListener, uploader, storage);
    }
}
