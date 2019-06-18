package fm.doe.national.remote_storage.data.accessor;

import io.reactivex.Single;

public interface RemoteStorageAccessor {

    String EMPTY_CONTENT = "";

    void auth();

    void scheduleUploading(long surveyId);

    Single<String> requestContentFromDefaultStorage();

    Single<String> requestContentFromRemoteStorage();

    void onContentReceived(String content);
}
