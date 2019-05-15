package fm.doe.national.app_support.utils;

import android.app.Activity;

import androidx.annotation.Nullable;

public interface LifecycleListener {
    @Nullable
    Activity getCurrentActivity();
}
