package fm.doe.national.core.di.modules;

import android.content.Context;
import android.content.SharedPreferences;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.core.data.data_source.DataSource;
import fm.doe.national.core.data.data_source.RoomDataSource;
import fm.doe.national.core.di.CoreScope;
import fm.doe.national.core.interactors.SurveyInteractor;
import fm.doe.national.core.interactors.SurveyInteractorImpl;

import static android.content.Context.MODE_PRIVATE;

@Module
public class CoreModule {
    private static final String NAME_APP_PREFS_GLOBAL = "APP_PREFS_GLOBAL";

    @Provides
    @CoreScope
    public DataSource provideDataSource(Context context) {
        return new RoomDataSource(context);
    }

    @Provides
    @CoreScope
    public SurveyInteractor provideSurveyInteractor(DataSource dataSource) {
        return new SurveyInteractorImpl(dataSource);
    }

    @Provides
    @CoreScope
    public SharedPreferences provideSharedPreferences(Context context) {
        return context.getSharedPreferences(NAME_APP_PREFS_GLOBAL, MODE_PRIVATE);
    }
}
