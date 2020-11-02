package org.pacific_emis.surveys.core.utils;

import android.app.Activity;

import androidx.annotation.Nullable;

public interface LifecycleListener {
    @Nullable
    Activity getCurrentActivity();
}
