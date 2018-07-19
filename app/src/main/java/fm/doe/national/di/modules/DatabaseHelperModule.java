package fm.doe.national.di.modules;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.data_source.db.dao.DatabaseHelper;

@Module(includes = ContextModule.class)
public class DatabaseHelperModule {

    @Provides
    @Singleton
    public DatabaseHelper provideDatabaseHelper(Context context) {
        return OpenHelperManager.getHelper(context, DatabaseHelper.class);
    }

}
