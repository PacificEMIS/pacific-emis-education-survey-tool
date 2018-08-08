package fm.doe.national.di.modules;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.data.converters.DataImporter;
import fm.doe.national.data.converters.SchoolImporter;
import fm.doe.national.data.data_source.db.dao.DatabaseHelper;

@Module(includes = {DatabaseHelperModule.class, GsonModule.class})
public class ConvertersModule {

    @Provides
    @Singleton
    public DataImporter provideJsonImporter(Gson gson, DatabaseHelper helper) {
        return new SchoolImporter(gson, helper);
    }

}
