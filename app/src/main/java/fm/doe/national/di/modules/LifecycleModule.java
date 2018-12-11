package fm.doe.national.di.modules;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.utils.ActivityLifecycleListener;
import fm.doe.national.utils.LifecycleListener;

@Module
public class LifecycleModule {
    @Provides
    @Singleton
    public LifecycleListener provideLifecycleListener(Context context) {
        return new ActivityLifecycleListener(context);
    }
}
