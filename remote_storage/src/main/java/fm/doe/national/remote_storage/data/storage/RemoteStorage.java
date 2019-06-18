package fm.doe.national.remote_storage.data.storage;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface RemoteStorage {

    Single<Object> requestStorageFileTree();

    Completable uploadContent(String content, String path);

    Single<String> downloadContent(String path);

}
