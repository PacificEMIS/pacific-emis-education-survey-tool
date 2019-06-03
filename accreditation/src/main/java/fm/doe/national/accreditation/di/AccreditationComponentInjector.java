package fm.doe.national.accreditation.di;

public class AccreditationComponentInjector {
    public static AccreditationComponent getComponent(Object injectionSource) {
        if (injectionSource instanceof AccreditationComponentProvider) {
            return ((AccreditationComponentProvider) injectionSource).provideAccreditationComponent();
        } else {
            throw new IllegalStateException("Injection source is not a AccreditationComponentProvider");
        }
    }
}
