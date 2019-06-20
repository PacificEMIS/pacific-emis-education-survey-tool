package fm.doe.national.offline_sync.di;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.core.data.data_source.DataSource;
import fm.doe.national.core.utils.LifecycleListener;
import fm.doe.national.offline_sync.data.accessor.BluetoothOfflineAccessor;
import fm.doe.national.offline_sync.data.accessor.OfflineAccessor;
import fm.doe.national.offline_sync.domain.InteractiveOfflineSyncUseCaseImpl;
import fm.doe.national.offline_sync.domain.OfflineSyncUseCase;

@Module
public class OfflineSyncModule {

    @Provides
    @OfflineSyncScope
    OfflineAccessor provideOfflineAccessor() {
        return new BluetoothOfflineAccessor();
    }

    @Provides
    @OfflineSyncScope
    OfflineSyncUseCase provideUseCase(LifecycleListener lifecycleListener) {
        return new InteractiveOfflineSyncUseCaseImpl(lifecycleListener);
    }
}
