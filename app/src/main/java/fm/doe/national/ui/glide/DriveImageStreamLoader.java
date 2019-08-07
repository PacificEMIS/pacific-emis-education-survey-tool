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

public class DriveImageStreamLoader implements ModelLoader<ImageModel, InputStream> {

    private final RemoteStorage remoteStorage;

    public DriveImageStreamLoader(RemoteStorage remoteStorage) {
        this.remoteStorage = remoteStorage;
    }

    @Nullable
    @Override
    public LoadData<InputStream> buildLoadData(@NonNull ImageModel imageModel,
                                               int width,
                                               int height,
                                               @NonNull Options options) {
        String fileId = imageModel.getFileId();
        return new ModelLoader.LoadData<>(
                new ObjectKey(fileId),
                new ImageStreamFetcher(remoteStorage, fileId)
        );
    }

    @Override
    public boolean handles(@NonNull ImageModel imageModel) {
        return true;
    }

    public static class Factory implements ModelLoaderFactory<ImageModel, InputStream> {

        private final RemoteStorage remoteStorage;

        public Factory(RemoteStorage remoteStorage) {
            this.remoteStorage = remoteStorage;
        }

        @NonNull
        @Override
        public ModelLoader<ImageModel, InputStream> build(@NonNull MultiModelLoaderFactory multiFactory) {
            return new DriveImageStreamLoader(remoteStorage);
        }

        @Override
        public void teardown() {
            // Do nothing, this instance doesn't own the client.
        }
    }
}
