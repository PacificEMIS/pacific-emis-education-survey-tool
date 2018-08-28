package fm.doe.national.di;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Component;
import fm.doe.national.data.data_source.models.SchoolAccreditation;
import fm.doe.national.data.parsers.Parser;
import fm.doe.national.data.serializers.Serializer;
import fm.doe.national.di.modules.AccreditationDataSourceModule;
import fm.doe.national.di.modules.ContextModule;
import fm.doe.national.di.modules.DatabaseHelperModule;
import fm.doe.national.di.modules.GsonModule;
import fm.doe.national.di.modules.ParsersModule;
import fm.doe.national.di.modules.SerializersModule;
import fm.doe.national.ui.screens.main.MainActivity;

@Singleton
@Component(modules = {ContextModule.class,
        DatabaseHelperModule.class,
        AccreditationDataSourceModule.class,
        GsonModule.class,
        ParsersModule.class,
        SerializersModule.class})
public interface AppComponent {

    Gson getGson();
    Parser<SchoolAccreditation> getSchoolAccreditationParser();
    Serializer<SchoolAccreditation> getSchoolAccreditationSerizlizer();
    void inject(MainActivity mainActivity);
}
