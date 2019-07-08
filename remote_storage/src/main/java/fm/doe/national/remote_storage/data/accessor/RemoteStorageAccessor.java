package fm.doe.national.remote_storage.data.accessor;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface RemoteStorageAccessor {

    String EMPTY_CONTENT = "";

    Completable signInAsUser();

    void signOutAsUser();

    void scheduleUploading(long surveyId);

    Single<String> requestContentFromDefaultStorage();

    Single<String> requestContentFromRemoteStorage();

    void onContentReceived(String content);

}
