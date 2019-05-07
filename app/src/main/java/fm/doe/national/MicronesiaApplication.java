package fm.doe.national;


import androidx.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;

import fm.doe.national.di.AppComponent;
import fm.doe.national.di.DaggerAppComponent;
import fm.doe.national.di.modules.ContextModule;
import io.fabric.sdk.android.Fabric;

public class MicronesiaApplication extends MultiDexApplication {

    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        appComponent = DaggerAppComponent.builder()
                .contextModule(new ContextModule(this))
                .build();
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }

}
