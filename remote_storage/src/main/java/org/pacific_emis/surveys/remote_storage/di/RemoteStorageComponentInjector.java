package org.pacific_emis.surveys.remote_storage.di;

public class RemoteStorageComponentInjector {
    public static RemoteStorageComponent getComponent(Object injectionSource) {
        if (injectionSource instanceof RemoteStorageComponentProvider) {
            return ((RemoteStorageComponentProvider) injectionSource).provideRemoteStorageComponent();
        } else {
            throw new IllegalStateException("Injection source is not a RemoteStorageComponentProvider");
        }
    }
}
