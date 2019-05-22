package fm.doe.national.app_support.di.modules;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.core.utils.ActivityLifecycleListener;
import fm.doe.national.core.utils.LifecycleListener;
import fm.doe.national.core.di.FeatureScope;

@Module
public class LifecycleModule {
    @Provides
    @FeatureScope
    public LifecycleListener provideLifecycleListener(Context context) {
        return new ActivityLifecycleListener(context);
    }
}
