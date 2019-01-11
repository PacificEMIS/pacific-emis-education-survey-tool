package fm.doe.national.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import java.lang.ref.WeakReference;


import androidx.annotation.Nullable;
import fm.doe.national.MicronesiaApplication;

public class ActivityLifecycleListener implements LifecycleListener, Application.ActivityLifecycleCallbacks {

    private WeakReference<Activity> currentActivityRef = null;

    public ActivityLifecycleListener(Context applicationContext) {
        ((MicronesiaApplication)applicationContext).registerActivityLifecycleCallbacks(this);
    }

    @Override
    @Nullable
    public Activity getCurrentActivity() {
        if (currentActivityRef == null) return null;
        return currentActivityRef.get();
    }

    @Override
    public void onActivityStarted(Activity activity) {
        currentActivityRef = new WeakReference<>(activity);
    }

    @Override
    public void onActivityResumed(Activity activity) {
        currentActivityRef = new WeakReference<>(activity);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        // nothing
    }

    @Override
    public void onActivityPaused(Activity activity) {
        // nothing
    }

    @Override
    public void onActivityStopped(Activity activity) {
        // nothing
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        // nothing
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        // nothing
    }
}
