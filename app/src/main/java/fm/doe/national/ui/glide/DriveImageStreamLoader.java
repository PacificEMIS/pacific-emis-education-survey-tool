package fm.doe.national.ui.glide;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.signature.ObjectKey;

import java.io.InputStream;

import fm.doe.national.core.ui.glide.ImageModel;
import fm.doe.national.remote_storage.data.storage.RemoteStorage;

public class DriveImageStreamLoader implements ModelLoader<String, InputStream> {

    private static final String REGEX_LOADER_FORMAT = "^[^/.]+$";
    private final RemoteStorage remoteStorage;

    public DriveImageStreamLoader(RemoteStorage remoteStorage) {
        this.remoteStorage = remoteStorage;
    }

    @Nullable
    @Override
    public LoadData<InputStream> buildLoadData(@NonNull String imageModel,
                                               int width,
                                               int height,
                                               @NonNull Options options) {
        return new ModelLoader.LoadData<>(
                new ObjectKey(imageModel),
                new ImageStreamFetcher(remoteStorage, imageModel)
        );
    }

    @Override
    public boolean handles(@NonNull String imageModel) {
        return imageModel.matches(REGEX_LOADER_FORMAT);
    }

    public static class Factory implements ModelLoaderFactory<String, InputStream> {

        private final RemoteStorage remoteStorage;

        public Factory(RemoteStorage remoteStorage) {
            this.remoteStorage = remoteStorage;
        }

        @NonNull
        @Override
        public ModelLoader<String, InputStream> build(@NonNull MultiModelLoaderFactory multiFactory) {
            return new DriveImageStreamLoader(remoteStorage);
        }

        @Override
        public void teardown() {
            // Do nothing, this instance doesn't own the client.
        }
    }
}
