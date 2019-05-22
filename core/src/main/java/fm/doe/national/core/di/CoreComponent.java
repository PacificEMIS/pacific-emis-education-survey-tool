package fm.doe.national.core.di;

import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Component;
import fm.doe.national.core.data.data_source.DataSource;
import fm.doe.national.core.interactors.SurveyInteractor;

@Singleton
@Component(modules = {
        CoreModule.class
})
public interface CoreComponent {

    DataSource getDataSource();

    SurveyInteractor getSurveyInteractor();

    SharedPreferences getSharedPreferences();

}
