package fm.doe.national.app_support.di.modules;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.core.di.FeatureScope;

@Module
public class ContextModule {

    private final Context context;

    public ContextModule(Context context) {
        this.context = context;
    }

    @Provides
    @FeatureScope
    public Context provideContext() {
        return context;
    }

}
