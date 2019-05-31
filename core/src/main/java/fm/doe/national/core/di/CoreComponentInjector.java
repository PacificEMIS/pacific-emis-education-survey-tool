package fm.doe.national.core.di;

public class CoreComponentInjector {
    public static CoreComponent getComponent(Object injectionSource) {
        if (injectionSource instanceof CoreComponentProvider) {
            return ((CoreComponentProvider) injectionSource).provideCoreComponent();
        } else {
            throw new IllegalStateException("Injection source is not a CoreComponentProvider");
        }
    }
}
