package fm.doe.national.remote_storage.data.storage;

import io.reactivex.Completable;
import io.reactivex.Single;

public final class FirebaseRemoteStorage implements RemoteStorage {

    @Override
    public Single<Object> requestStorageFileTree() {
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
