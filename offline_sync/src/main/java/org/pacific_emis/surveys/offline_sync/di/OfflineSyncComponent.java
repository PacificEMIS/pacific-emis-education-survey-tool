package org.pacific_emis.surveys.offline_sync.di;

import dagger.Component;
import org.pacific_emis.surveys.core.di.CoreComponent;
import org.pacific_emis.surveys.offline_sync.data.accessor.OfflineAccessor;
import org.pacific_emis.surveys.offline_sync.domain.OfflineSyncUseCase;
import org.pacific_emis.surveys.offline_sync.domain.SyncNotifier;

@OfflineSyncScope
@Component(modules = {
        OfflineSyncModule.class
}, dependencies = {
        CoreComponent.class
})
public interface OfflineSyncComponent {

    OfflineSyncUseCase getUseCase();

    OfflineAccessor getAccessor();

    SyncNotifier getNotifier();

}
