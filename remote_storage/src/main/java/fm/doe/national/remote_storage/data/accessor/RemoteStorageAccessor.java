package fm.doe.national.remote_storage.data.accessor;

import androidx.annotation.Nullable;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface RemoteStorageAccessor {

    String EMPTY_CONTENT = "";

    Completable signIn();

    void signOut();

    void scheduleUploading(long surveyId);

    Single<String> requestContentFromDefaultStorage();

    Single<String> requestContentFromRemoteStorage();

    void onContentReceived(String content);

    void onGoogleSignInAccountReceived(@Nullable GoogleSignInAccount account);
}
