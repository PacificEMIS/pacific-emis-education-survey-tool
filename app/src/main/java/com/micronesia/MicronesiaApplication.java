package com.micronesia;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.micronesia.di.AppComponent;
import com.micronesia.di.DaggerAppComponent;
import com.micronesia.di.modules.ContextModule;
import io.fabric.sdk.android.Fabric;

public class MicronesiaApplication extends Application {

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
