package fm.doe.national.remote_storage.data.accessor;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import fm.doe.national.core.data.exceptions.AuthenticationException;
import fm.doe.national.core.utils.LifecycleListener;
import fm.doe.national.remote_storage.R;
import fm.doe.national.remote_storage.data.storage.DriveRemoteStorage;
import fm.doe.national.remote_storage.data.uploader.RemoteUploader;
import fm.doe.national.remote_storage.ui.default_storage.DefaultStorageActivity;
import fm.doe.national.remote_storage.ui.remote_storage.DriveStorageActivity;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.CompletableSubject;
import io.reactivex.subjects.SingleSubject;

public final class RemoteStorageAccessorImpl implements RemoteStorageAccessor {

    private static final long EMPTY_EMIT_DELAY_MS = 500;

    private final LifecycleListener lifecycleListener;
    private final RemoteUploader uploader;
    private final DriveRemoteStorage driveRemoteStorage;

    private SingleSubject<String> contentSubject;
    private CompletableSubject authSubject;
    private GoogleSignInAccount account;

    public RemoteStorageAccessorImpl(LifecycleListener lifecycleListener,
                                     Context appContext,
                                     RemoteUploader uploader,
                                     DriveRemoteStorage driveRemoteStorage) {
        this.lifecycleListener = lifecycleListener;
        this.uploader = uploader;
        this.driveRemoteStorage = driveRemoteStorage;

        try {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            GsonFactory gsonFactory = new GsonFactory();
            GoogleCredential credential = GoogleCredential.fromStream(
                    appContext.getAssets().open("Micronesia-b9acf9c6198e.json"),
                    transport,
                    gsonFactory)
                    .createScoped(Arrays.asList(DriveScopes.DRIVE_FILE, DriveScopes.DRIVE_METADATA));
            Drive drive = new Drive.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), credential)
                    .setApplicationName(appContext.getString(R.string.app_name))
                    .build();
            this.driveRemoteStorage.init(drive);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Completable signInAsUser() {
        if (account != null) {
            return Completable.complete();
        }

        Activity currentActivity = lifecycleListener.getCurrentActivity();

        if (currentActivity == null) {
            authSubject.onComplete();
            return Completable.error(new AuthenticationException("No activity"));
        }

        return Completable.fromAction(() -> account = GoogleSignIn.getLastSignedInAccount(currentActivity));
    }

    @Override
    public void signOutAsUser() {
    }

    @Override
    public void scheduleUploading(long surveyId) {
        uploader.scheduleUploading(surveyId);
    }

    @Override
    public Single<String> requestContentFromDefaultStorage() {
        contentSubject = SingleSubject.create();

        return Completable.fromAction(() -> {
            Activity currentActivity = lifecycleListener.getCurrentActivity();

            if (currentActivity == null) {
                scheduleEmptyEmit();
                return;
            }

            currentActivity.startActivity(DefaultStorageActivity.createIntent(currentActivity));
        })
                .andThen(contentSubject);
    }

    private void scheduleEmptyEmit() {
        Schedulers.io().scheduleDirect(() -> onContentReceived(EMPTY_CONTENT), EMPTY_EMIT_DELAY_MS, TimeUnit.MILLISECONDS);
    }

    @Override
    public Single<String> requestContentFromRemoteStorage() {
        contentSubject = SingleSubject.create();

        return Completable.fromAction(() -> {
            Activity currentActivity = lifecycleListener.getCurrentActivity();

            if (currentActivity == null) {
                scheduleEmptyEmit();
                return;
            }

            currentActivity.startActivity(DriveStorageActivity.createIntent(currentActivity));
        })
                .andThen(contentSubject);
    }

    @Override
    public void onContentReceived(String content) {
        if (contentSubject != null) {
            contentSubject.onSuccess(content);
            contentSubject = null;
        }
    }
}
