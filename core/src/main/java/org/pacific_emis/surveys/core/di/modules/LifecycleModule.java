package org.pacific_emis.surveys.core.di.modules;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import org.pacific_emis.surveys.core.di.CoreScope;
import org.pacific_emis.surveys.core.utils.ActivityLifecycleListener;
import org.pacific_emis.surveys.core.utils.LifecycleListener;

@Module
public class LifecycleModule {
    @Provides
    @CoreScope
    LifecycleListener provideLifecycleListener(Context context) {
        return new ActivityLifecycleListener(context);
    }
}
