package fm.doe.national.remote_storage.data.accessor;

import android.app.Activity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.concurrent.TimeUnit;

import fm.doe.national.core.data.exceptions.AuthenticationException;
import fm.doe.national.core.data.exceptions.PickerDeclinedException;
import fm.doe.national.core.utils.LifecycleListener;
import fm.doe.national.remote_storage.BuildConfig;
import fm.doe.national.remote_storage.data.uploader.RemoteUploader;
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

    private SingleSubject<String> contentSubject;
    private CompletableSubject authSubject;
    private GoogleSignInAccount account;

    public RemoteStorageAccessorImpl(LifecycleListener lifecycleListener,
                                     RemoteUploader uploader) {
        this.lifecycleListener = lifecycleListener;
        this.uploader = uploader;
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

    private void scheduleEmptyEmit() {
        Schedulers.io().scheduleDirect(() -> onContentReceived(EMPTY_CONTENT), EMPTY_EMIT_DELAY_MS, TimeUnit.MILLISECONDS);
    }

    @Override
    public Single<String> requestContentFromStorage() {
        contentSubject = SingleSubject.create();

        return Completable.fromAction(() -> {
            Activity currentActivity = lifecycleListener.getCurrentActivity();

            if (currentActivity == null) {
                scheduleEmptyEmit();
                return;
            }

            currentActivity.startActivity(DriveStorageActivity.createIntent(currentActivity, false));
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

    @Override
    public void onContentNotReceived() {
        if (contentSubject != null) {
            contentSubject.onError(new PickerDeclinedException());
            contentSubject = null;
        }
    }

    @Override
    public void showDebugStorage() {
        if (!BuildConfig.DEBUG) {
            throw new IllegalStateException();
        }

        Activity currentActivity = lifecycleListener.getCurrentActivity();

        if (currentActivity == null) {
            scheduleEmptyEmit();
            return;
        }

        currentActivity.startActivity(DriveStorageActivity.createIntent(currentActivity, true));
    }
}
