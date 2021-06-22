package org.pacific_emis.surveys.remote_data.di;

public class RemoteDataComponentInjector {
    public static RemoteDataComponent getComponent(Object injectionSource) {
        if (injectionSource instanceof RemoteDataComponentProvider) {
            return ((RemoteDataComponentProvider) injectionSource).provideRemoteDataComponent();
        } else {
            throw new IllegalStateException("Injection source is not a RemoteDataComponentProvider");
        }
    }
}
