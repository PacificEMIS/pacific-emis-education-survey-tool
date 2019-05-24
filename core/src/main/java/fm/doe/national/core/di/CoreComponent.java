package fm.doe.national.core.di;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

import dagger.Component;
import fm.doe.national.core.data.data_source.DataSource;
import fm.doe.national.core.data.files.PicturesRepository;
import fm.doe.national.core.data.model.School;
import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.data.serialization.parsers.Parser;
import fm.doe.national.core.data.serialization.serializers.Serializer;
import fm.doe.national.core.di.modules.ContextModule;
import fm.doe.national.core.di.modules.CoreModule;
import fm.doe.national.core.di.modules.LifecycleModule;
import fm.doe.national.core.di.modules.LocalDataSourceModule;
import fm.doe.national.core.di.modules.PreferencesModule;
import fm.doe.national.core.di.modules.SerializersModule;
import fm.doe.national.core.interactors.SurveyInteractor;
import fm.doe.national.core.preferences.GlobalPreferences;
import fm.doe.national.core.utils.LifecycleListener;

@CoreScope
@Component(modules = {
        CoreModule.class,
        PreferencesModule.class,
        ContextModule.class,
        LocalDataSourceModule.class,
        SerializersModule.class,
        LifecycleModule.class
})
public interface CoreComponent {

    Context getContext();

    Parser<Survey> getSurveyParser();

    Parser<List<School>> getSchoolsParser();

    Serializer<Survey> getSurveySerializer();

    PicturesRepository getPicturesRepository();

    GlobalPreferences getGlobalPreferences();

    DataSource getDataSource();

    SurveyInteractor getSurveyInteractor();

    SharedPreferences getSharedPreferences();

    LifecycleListener getLifecycleListener();
}
