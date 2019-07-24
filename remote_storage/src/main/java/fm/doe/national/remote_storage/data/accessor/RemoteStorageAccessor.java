package fm.doe.national.remote_storage.data.accessor;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import javax.annotation.Nullable;

import fm.doe.national.accreditation_core.data.model.AccreditationSurvey;
import fm.doe.national.remote_storage.data.model.ExportType;
import io.reactivex.Completable;
import io.reactivex.Single;

public interface RemoteStorageAccessor {

    String EMPTY_CONTENT = "";

    Completable signInAsUser();

    void signOutAsUser();

    void scheduleUploading(long surveyId);

    Single<String> requestContentFromStorage();

    void onContentReceived(String content);

    void onContentNotReceived();

    void showDebugStorage();

    void onGoogleSignInAccountReceived(GoogleSignInAccount account);

    @Nullable
    String getUserEmail();

    Completable exportToExcel(AccreditationSurvey survey, ExportType exportType);
}
