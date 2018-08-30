package fm.doe.national.utils;

import android.app.Activity;
import android.support.annotation.Nullable;

public interface LifecycleListener {
    @Nullable
    Activity getCurrentActivity();
}
