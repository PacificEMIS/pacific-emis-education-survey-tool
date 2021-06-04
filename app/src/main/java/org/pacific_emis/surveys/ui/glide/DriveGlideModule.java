package org.pacific_emis.surveys.ui.glide;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;

import org.pacific_emis.surveys.remote_storage.di.RemoteStorageComponentInjector;

@GlideModule
public class DriveGlideModule extends AppGlideModule {

    @Override
    public void registerComponents(@NonNull Context context,
                                   @NonNull Glide glide,
                                   @NonNull Registry registry) {
        registry.append(
                String.class,
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
