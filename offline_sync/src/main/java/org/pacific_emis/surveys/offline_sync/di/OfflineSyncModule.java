package org.pacific_emis.surveys.offline_sync.di;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import org.pacific_emis.surveys.accreditation_core.di.AccreditationCoreComponent;
import org.pacific_emis.surveys.core.data.files.FilesRepository;
import org.pacific_emis.surveys.core.preferences.LocalSettings;
import org.pacific_emis.surveys.core.utils.LifecycleListener;
import org.pacific_emis.surveys.offline_sync.data.accessor.BluetoothOfflineAccessor;
import org.pacific_emis.surveys.offline_sync.data.accessor.OfflineAccessor;
import org.pacific_emis.surveys.offline_sync.domain.OfflineSyncUseCaseImpl;
import org.pacific_emis.surveys.offline_sync.domain.OfflineSyncUseCase;
import org.pacific_emis.surveys.offline_sync.domain.SyncNotifier;
import org.pacific_emis.surveys.wash_core.di.WashCoreComponent;

@Module
public class OfflineSyncModule {

    private final WashCoreComponent washCoreComponent;
    private final AccreditationCoreComponent accreditationCoreComponent;

    public OfflineSyncModule(WashCoreComponent washCoreComponent, AccreditationCoreComponent accreditationCoreComponent) {
        this.washCoreComponent = washCoreComponent;
        this.accreditationCoreComponent = accreditationCoreComponent;
    }

    @Provides
    @OfflineSyncScope
    OfflineAccessor provideOfflineAccessor(Context context,
                                           LocalSettings localSettings,
                                           FilesRepository filesRepository,
                                           SyncNotifier syncNotifier) {
        return new BluetoothOfflineAccessor(
                context,
                localSettings,
                accreditationCoreComponent.getDataSource(),
                washCoreComponent.getDataSource(),
                filesRepository,
                syncNotifier
        );
    }

    @Provides
    @OfflineSyncScope
    OfflineSyncUseCase provideUseCase(LifecycleListener lifecycleListener, OfflineAccessor offlineAccessor) {
        return new OfflineSyncUseCaseImpl(lifecycleListener, offlineAccessor);
    }

    @Provides
    @OfflineSyncScope
    SyncNotifier provideSyncNotifier() {
        return new SyncNotifier();
    }
}
