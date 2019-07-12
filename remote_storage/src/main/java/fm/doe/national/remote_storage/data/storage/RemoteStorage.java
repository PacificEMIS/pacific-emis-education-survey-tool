package fm.doe.national.remote_storage.data.storage;


import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.List;

import javax.annotation.Nullable;

import fm.doe.national.core.data.model.Survey;
import fm.doe.national.remote_storage.data.model.GoogleDriveFileHolder;
import io.reactivex.Completable;
import io.reactivex.Single;

public interface RemoteStorage {

    Single<List<GoogleDriveFileHolder>> requestStorageFiles(String parentFolderId);

    Completable upload(Survey survey);

    Single<String> loadContent(String fileId);

    Completable delete(String fileId);

    void refreshCredentials();

    @Nullable
    GoogleSignInAccount getUserAccount();

    void setUserAccount(@Nullable GoogleSignInAccount account);
}
