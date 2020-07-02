package fm.doe.national.core.di;

import android.content.Context;
import android.content.SharedPreferences;

import org.simpleframework.xml.core.Persister;

import java.util.List;

import dagger.Component;
import fm.doe.national.core.data.files.FilesRepository;
import fm.doe.national.core.data.model.School;
import fm.doe.national.core.data.serialization.Parser;
import fm.doe.national.core.di.modules.ContextModule;
import fm.doe.national.core.di.modules.LifecycleModule;
import fm.doe.national.core.di.modules.LocalDataSourceModule;
import fm.doe.national.core.di.modules.PreferencesModule;
import fm.doe.national.core.di.modules.SerializationModule;
import fm.doe.national.core.preferences.LocalSettings;
import fm.doe.national.core.utils.LifecycleListener;

@CoreScope
@Component(modules = {
        PreferencesModule.class,
        ContextModule.class,
        LocalDataSourceModule.class,
        LifecycleModule.class,
        SerializationModule.class
})
public interface CoreComponent {

    Context getContext();

    FilesRepository getFilesRepository();

    LocalSettings getLocalSettings();

    SharedPreferences getSharedPreferences();

    LifecycleListener getLifecycleListener();

    Parser<List<School>> getSchoolsParser();

    Persister getPersister();

}
