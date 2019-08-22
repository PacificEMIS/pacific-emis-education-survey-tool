package fm.doe.national.core.utils;

import android.app.Activity;

import androidx.annotation.Nullable;

public interface LifecycleListener {
    @Nullable
    Activity getCurrentActivity();
}
