package fm.doe.national.app_support;


import androidx.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;

import fm.doe.national.app_support.di.AppComponent;
import fm.doe.national.app_support.di.DaggerAppComponent;
import fm.doe.national.app_support.di.modules.ContextModule;
import fm.doe.national.core.di.CoreComponent;
import fm.doe.national.core.di.CoreComponentProvider;
import fm.doe.national.core.di.CoreInjector;
import fm.doe.national.core.di.CoreModule;
import fm.doe.national.core.di.DaggerCoreComponent;
import io.fabric.sdk.android.Fabric;

public class MicronesiaApplication extends MultiDexApplication implements CoreComponentProvider {

    private static AppComponent appComponent;

    private CoreComponent coreComponent = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        appComponent = DaggerAppComponent.builder()
                .contextModule(new ContextModule(this))
                .coreComponent(CoreInjector.provideCoreComponent(this))
                .build();
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    public CoreComponent provideCoreComponent() {
        if (coreComponent == null) {
            coreComponent = DaggerCoreComponent.builder()
                    .coreModule(new CoreModule(this))
                    .build();
        }
        return coreComponent;
    }
}
