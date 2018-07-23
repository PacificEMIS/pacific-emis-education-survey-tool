package fm.doe.national.di.modules;

import android.content.Context;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.data_source.static_source.StaticDataSource;

@Module(includes = {ContextModule.class, GsonModule.class})
public class StaticDataSourceModule {

    @Provides
    @Singleton
    public StaticDataSource provideStaticDataSource(Context context, Gson gson) {
        return new StaticDataSource(context, gson);
    }

}
