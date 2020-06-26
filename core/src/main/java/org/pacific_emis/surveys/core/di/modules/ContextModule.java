package org.pacific_emis.surveys.core.di.modules;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import org.pacific_emis.surveys.core.di.CoreScope;

@Module
public class ContextModule {

    private final Context context;

    public ContextModule(Context context) {
        this.context = context;
    }

    @Provides
    @CoreScope
    public Context provideContext() {
        return context;
    }

}
