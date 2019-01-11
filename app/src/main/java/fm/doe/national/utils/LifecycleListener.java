package fm.doe.national.utils;

import android.app.Activity;

import androidx.annotation.Nullable;

public interface LifecycleListener {
    @Nullable
    Activity getCurrentActivity();
}
