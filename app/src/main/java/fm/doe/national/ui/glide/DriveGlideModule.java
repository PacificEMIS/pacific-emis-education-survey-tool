package fm.doe.national.ui.glide;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;

import fm.doe.national.core.ui.glide.ImageModel;
import fm.doe.national.remote_storage.di.RemoteStorageComponentInjector;
import fm.doe.national.ui.glide.DriveImageStreamLoader;

@GlideModule
public class DriveGlideModule extends AppGlideModule {

    @Override
    public void registerComponents(@NonNull Context context,
                                   @NonNull Glide glide,
                                   @NonNull Registry registry) {
        registry.append(
                ImageModel.class,
                InputStream.class,
                new DriveImageStreamLoader.Factory(
                        RemoteStorageComponentInjector.getComponent(context.getApplicationContext())
                                .getRemoteStorage()
                )
        );

        super.registerComponents(context, glide, registry);
    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
