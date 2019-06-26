package fm.doe.national.offline_sync.di;

import dagger.Component;
import fm.doe.national.core.di.CoreComponent;
import fm.doe.national.offline_sync.data.accessor.OfflineAccessor;
import fm.doe.national.offline_sync.domain.OfflineSyncUseCase;

@OfflineSyncScope
@Component(modules = {
        OfflineSyncModule.class
}, dependencies = {
        CoreComponent.class
})
public interface OfflineSyncComponent {

    OfflineSyncUseCase getUseCase();

    OfflineAccessor getAccessor();

}
