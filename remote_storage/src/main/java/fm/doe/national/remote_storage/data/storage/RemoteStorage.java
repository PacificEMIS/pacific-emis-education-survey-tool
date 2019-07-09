package fm.doe.national.remote_storage.data.storage;


import java.util.List;

import fm.doe.national.core.preferences.entities.AppRegion;
import fm.doe.national.core.preferences.entities.OperatingMode;
import fm.doe.national.remote_storage.data.model.GoogleDriveFileHolder;
import io.reactivex.Completable;
import io.reactivex.Single;

public interface RemoteStorage {

    Single<List<GoogleDriveFileHolder>> requestStorageFiles(String parentFolderId);

    Completable uploadContent(String content, String filename, AppRegion appRegion, OperatingMode operatingMode);

    Single<String> loadContent(String fileId);

    Completable delete(String fileId);
}
