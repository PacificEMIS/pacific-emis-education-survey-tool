package org.pacific_emis.surveys.remote_storage.data.accessor;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import javax.annotation.Nullable;

import org.pacific_emis.surveys.accreditation_core.data.model.AccreditationSurvey;
import org.pacific_emis.surveys.remote_storage.data.model.ExportType;
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

    Single<String> exportToExcel(AccreditationSurvey survey, ExportType exportType);

    Single<String> fetchStartPageToken();
}
