package fm.doe.national.core.di.modules;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.core.data.files.FileFilesRepository;
import fm.doe.national.core.data.files.FilesRepository;
import fm.doe.national.core.di.CoreScope;

@Module
public class LocalDataSourceModule {

    @Provides
    @CoreScope
    public FilesRepository providePicturesRepository(Context context) {
        return new FileFilesRepository(context);
    }

}
