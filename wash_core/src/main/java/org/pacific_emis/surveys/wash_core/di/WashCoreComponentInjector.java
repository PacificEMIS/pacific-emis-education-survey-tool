package org.pacific_emis.surveys.wash_core.di;

public class WashCoreComponentInjector {
    public static WashCoreComponent getComponent(Object injectionSource) {
        if (injectionSource instanceof WashCoreComponentProvider) {
            return ((WashCoreComponentProvider) injectionSource).provideWashCoreComponent();
        } else {
            throw new IllegalStateException("Injection source is not a WashCoreComponentProvider");
        }
    }
}
