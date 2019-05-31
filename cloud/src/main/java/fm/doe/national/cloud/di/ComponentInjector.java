package fm.doe.national.cloud.di;

public class ComponentInjector {
    public static CloudComponent getComponent(Object injectionSource) {
        if (injectionSource instanceof CloudComponentProvider) {
            return ((CloudComponentProvider) injectionSource).provideCloudComponent();
        } else {
            throw new IllegalStateException("Injection source is not a CloudComponentProvider");
        }
    }
}
