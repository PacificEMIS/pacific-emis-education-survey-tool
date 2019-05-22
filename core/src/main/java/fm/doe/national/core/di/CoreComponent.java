package fm.doe.national.core.di;

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

}
