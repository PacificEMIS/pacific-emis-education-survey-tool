package org.pacific_emis.surveys.ui.glide;

import androidx.annotation.NonNull;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;

import java.io.IOException;
import java.io.InputStream;

import org.pacific_emis.surveys.remote_storage.data.storage.RemoteStorage;

public class ImageStreamFetcher implements DataFetcher<InputStream> {

    private final RemoteStorage remoteStorage;
    private final String fileId;

    public ImageStreamFetcher(RemoteStorage remoteStorage, String fileId) {
        this.remoteStorage = remoteStorage;
        this.fileId = fileId;
    }

    @Override
    public void loadData(@NonNull Priority priority, @NonNull DataCallback<? super InputStream> callback) {
        try {
            callback.onDataReady(remoteStorage.getFileContentStream(fileId));
        } catch (IOException e) {
            callback.onLoadFailed(e);
        }
    }

    @Override
    public void cleanup() {
        // nothing
    }

    @Override
    public void cancel() {
        // nothing
    }

    @NonNull
    @Override
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }

    @NonNull
    @Override
    public DataSource getDataSource() {
        return DataSource.REMOTE;
    }

}
