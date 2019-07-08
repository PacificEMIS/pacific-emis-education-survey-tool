package fm.doe.national.remote_storage.data.storage;


import java.util.List;

import fm.doe.national.remote_storage.data.model.GoogleDriveFileHolder;
import io.reactivex.Completable;
import io.reactivex.Single;

public interface RemoteStorage {

    Single<List<GoogleDriveFileHolder>> requestStorageFileTree();

    Completable uploadContent(String content, String path);

    Single<String> downloadContent(String path);

}
