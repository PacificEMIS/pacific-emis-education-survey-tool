package fm.doe.national.core.di.modules;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.core.di.CoreScope;
import fm.doe.national.core.utils.ActivityLifecycleListener;
import fm.doe.national.core.utils.LifecycleListener;

@Module
public class LifecycleModule {
    @Provides
    @CoreScope
    LifecycleListener provideLifecycleListener(Context context) {
        return new ActivityLifecycleListener(context);
    }
}
