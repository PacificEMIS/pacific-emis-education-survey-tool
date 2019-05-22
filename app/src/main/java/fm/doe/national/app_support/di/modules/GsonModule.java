package fm.doe.national.app_support.di.modules;

import com.google.gson.Gson;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.core.di.FeatureScope;

@Module
public class GsonModule {

    @Provides
    @FeatureScope
    public Gson provideGson() {
        return new Gson();
    }

}
