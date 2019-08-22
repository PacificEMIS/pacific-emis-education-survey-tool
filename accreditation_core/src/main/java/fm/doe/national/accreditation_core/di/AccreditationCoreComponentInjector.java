package fm.doe.national.accreditation_core.di;

public class AccreditationCoreComponentInjector {
    public static AccreditationCoreComponent getComponent(Object injectionSource) {
        if (injectionSource instanceof AccreditationCoreComponentProvider) {
            return ((AccreditationCoreComponentProvider) injectionSource).provideAccreditationCoreComponent();
        } else {
            throw new IllegalStateException("Injection source is not a AccreditationCoreComponentProvider");
        }
    }
}
