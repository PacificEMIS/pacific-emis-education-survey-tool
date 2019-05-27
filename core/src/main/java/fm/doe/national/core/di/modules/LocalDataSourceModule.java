package fm.doe.national.core.di.modules;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.core.data.files.FilePicturesRepository;
import fm.doe.national.core.data.files.PicturesRepository;
import fm.doe.national.core.di.CoreScope;

@Module
public class LocalDataSourceModule {

    @Provides
    @CoreScope
    public PicturesRepository providePicturesRepository(Context context) {
        return new FilePicturesRepository(context);
    }

}
