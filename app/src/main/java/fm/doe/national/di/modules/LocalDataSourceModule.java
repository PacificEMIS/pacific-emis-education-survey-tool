package fm.doe.national.di.modules;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.data.data_source.DataSource;
import fm.doe.national.data.data_source.RoomDataSource;
import fm.doe.national.data.files.FilePicturesRepository;
import fm.doe.national.data.files.PicturesRepository;

@Module
public class LocalDataSourceModule {

    @Provides
    @Singleton
    public DataSource provideAccreditationDataSource(Context context) {
        return new RoomDataSource(context);
    }

    @Provides
    @Singleton
    public PicturesRepository providePicturesRepository(Context context) {
        return new FilePicturesRepository(context);
    }

}
