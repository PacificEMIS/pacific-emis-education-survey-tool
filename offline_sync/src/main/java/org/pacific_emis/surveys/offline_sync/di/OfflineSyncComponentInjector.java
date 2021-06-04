package org.pacific_emis.surveys.offline_sync.di;

public class OfflineSyncComponentInjector {
    public static OfflineSyncComponent getComponent(Object injectionSource) {
        if (injectionSource instanceof OfflineSyncComponentProvider) {
            return ((OfflineSyncComponentProvider) injectionSource).provideOfflineSyncComponent();
        } else {
            throw new IllegalStateException("Injection source is not a OfflineSyncComponentProvider");
        }
    }
}
