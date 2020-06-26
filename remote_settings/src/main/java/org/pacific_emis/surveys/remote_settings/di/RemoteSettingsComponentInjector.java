package org.pacific_emis.surveys.remote_settings.di;

public class RemoteSettingsComponentInjector {
    public static RemoteSettingsComponent getComponent(Object injectionSource) {
        if (injectionSource instanceof RemoteSettingsComponentProvider) {
            return ((RemoteSettingsComponentProvider) injectionSource).provideRemoteSettingsComponent();
        } else {
            throw new IllegalStateException("Injection source is not a RemoteSettingsComponentProvider");
        }
    }
}
