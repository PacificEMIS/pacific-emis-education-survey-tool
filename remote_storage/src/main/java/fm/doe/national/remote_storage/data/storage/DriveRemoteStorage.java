package fm.doe.national.remote_storage.data.storage;

import java.util.List;

import fm.doe.national.remote_storage.data.model.GoogleDriveFileHolder;
import io.reactivex.Completable;
import io.reactivex.Single;

public final class DriveRemoteStorage implements RemoteStorage {

    @Override
    public Single<List<GoogleDriveFileHolder>> requestStorageFileTree() {
        return null;
    }

    @Override
    public Completable uploadContent(String content, String path) {
        return null;
    }

    @Override
    public Single<String> downloadContent(String path) {
        return null;
    }
}
