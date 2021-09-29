package org.pacific_emis.surveys.core.di;

import android.content.Context;
import android.content.SharedPreferences;

import org.pacific_emis.surveys.core.data.files.FilesRepository;
import org.pacific_emis.surveys.core.data.model.School;
import org.pacific_emis.surveys.core.data.remote_data_source.CoreRemoteDataSource;
import org.pacific_emis.surveys.core.data.serialization.Parser;
import org.pacific_emis.surveys.core.di.modules.ContextModule;
import org.pacific_emis.surveys.core.di.modules.LifecycleModule;
import org.pacific_emis.surveys.core.di.modules.LocalDataSourceModule;
import org.pacific_emis.surveys.core.di.modules.PreferencesModule;
import org.pacific_emis.surveys.core.di.modules.RemoteDataSourceModule;
import org.pacific_emis.surveys.core.di.modules.SerializationModule;
import org.pacific_emis.surveys.core.preferences.LocalSettings;
import org.pacific_emis.surveys.core.utils.LifecycleListener;
import org.simpleframework.xml.core.Persister;

import java.util.List;

import dagger.Component;

@CoreScope
@Component(modules = {
        PreferencesModule.class,
        ContextModule.class,
        LocalDataSourceModule.class,
        RemoteDataSourceModule.class,
        LifecycleModule.class,
        SerializationModule.class
})
public interface CoreComponent {

    Context getContext();

    FilesRepository getFilesRepository();

    CoreRemoteDataSource getRemoteDataSource();

    LocalSettings getLocalSettings();

    SharedPreferences getSharedPreferences();

    LifecycleListener getLifecycleListener();

    Parser<List<School>> getSchoolsParser();

    Persister getPersister();

}
