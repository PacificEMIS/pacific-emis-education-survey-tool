package fm.doe.national.app_support.di.modules;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.app_support.utils.ActivityLifecycleListener;
import fm.doe.national.app_support.utils.LifecycleListener;

@Module
public class LifecycleModule {
    @Provides
    @Singleton
    public LifecycleListener provideLifecycleListener(Context context) {
        return new ActivityLifecycleListener(context);
    }
}
