package org.pacific_emis.surveys.core.di.modules;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import org.pacific_emis.surveys.core.data.files.FilesRepositoryImpl;
import org.pacific_emis.surveys.core.data.files.FilesRepository;
import org.pacific_emis.surveys.core.di.CoreScope;

@Module
public class LocalDataSourceModule {

    @Provides
    @CoreScope
    public FilesRepository providePicturesRepository(Context context) {
        return new FilesRepositoryImpl(context);
    }

}
