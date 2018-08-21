package fm.doe.national;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

import javax.annotation.Nullable;

import fm.doe.national.di.AppComponent;
import fm.doe.national.di.DaggerAppComponent;
import fm.doe.national.di.modules.ContextModule;
import fm.doe.national.di.modules.DriveCloudAccessorModule;
import io.fabric.sdk.android.Fabric;

public class MicronesiaApplication extends Application implements Application.ActivityLifecycleCallbacks {

    private static AppComponent appComponent;
    private WeakReference<Activity> currentActivityRef;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        appComponent = DaggerAppComponent.builder()
                .contextModule(new ContextModule(this))
                .driveCloudAccessorModule(new DriveCloudAccessorModule())
                .build();
        registerActivityLifecycleCallbacks(this);

        showDebugDBAddressLogToast(this);
    }

    public static void showDebugDBAddressLogToast(Context context) {
        if (BuildConfig.DEBUG) {
            try {
                Class<?> debugDB = Class.forName("com.amitshekhar.DebugDB");
                Method getAddressLog = debugDB.getMethod("getAddressLog");
                Object value = getAddressLog.invoke(null);
                Toast.makeText(context, (String) value, Toast.LENGTH_LONG).show();
            } catch (Exception ignore) {
            }
        }
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    @Nullable
    public Activity getCurrentActivity() {
        return currentActivityRef.get();
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        Log.d("DEBUG", "s");
        currentActivityRef = new WeakReference<>(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Log.d("DEBUG", "s");
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.d("DEBUG", "s");
        currentActivityRef = new WeakReference<>(activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.d("DEBUG", "s");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.d("DEBUG", "s");
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        Log.d("DEBUG", "s");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.d("DEBUG", "s");    }
}
