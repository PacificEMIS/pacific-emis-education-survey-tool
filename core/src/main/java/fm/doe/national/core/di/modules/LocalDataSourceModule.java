package fm.doe.national.core.di.modules;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.core.data.files.FilesRepositoryImpl;
import fm.doe.national.core.data.files.FilesRepository;
import fm.doe.national.core.di.CoreScope;

@Module
public class LocalDataSourceModule {

    @Provides
    @CoreScope
    public FilesRepository providePicturesRepository(Context context) {
        return new FilesRepositoryImpl(context);
    }

}
