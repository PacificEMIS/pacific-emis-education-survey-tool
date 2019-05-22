package fm.doe.national.app_support.di.modules;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.core.di.FeatureScope;
import fm.doe.national.data.files.FilePicturesRepository;
import fm.doe.national.data.files.PicturesRepository;

@Module
public class LocalDataSourceModule {

    @Provides
    @FeatureScope
    public PicturesRepository providePicturesRepository(Context context) {
        return new FilePicturesRepository(context);
    }

}
