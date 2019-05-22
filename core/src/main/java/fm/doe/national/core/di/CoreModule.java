package fm.doe.national.core.di;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.core.data.data_source.DataSource;
import fm.doe.national.core.data.data_source.RoomDataSource;
import fm.doe.national.core.interactors.SurveyInteractor;
import fm.doe.national.core.interactors.SurveyInteractorImpl;

@Module
public class CoreModule {

    private final Context context;

    public CoreModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    public DataSource provideDataSource() {
        return new RoomDataSource(context);
    }

    @Provides
    @Singleton
    public SurveyInteractor provideSurveyInteractor(DataSource dataSource) {
        return new SurveyInteractorImpl(dataSource);
    }
}
