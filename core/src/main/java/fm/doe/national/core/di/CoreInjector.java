package fm.doe.national.core.di;

import android.content.Context;

public class CoreInjector {
    public static CoreComponent provideCoreComponent(Context applicationContext) {
        if (applicationContext instanceof CoreComponentProvider) {
            return ((CoreComponentProvider) applicationContext).provideCoreComponent();
        } else {
            throw new IllegalStateException("The application context you have passed does not implement CoreComponentProvider");
        }
    }
}
