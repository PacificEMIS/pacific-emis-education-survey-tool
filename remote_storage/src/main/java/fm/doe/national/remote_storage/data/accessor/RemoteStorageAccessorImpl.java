package fm.doe.national.remote_storage.data.accessor;

import android.app.Activity;

import androidx.annotation.Nullable;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.concurrent.TimeUnit;

import fm.doe.national.core.data.exceptions.AuthenticationException;
import fm.doe.national.core.utils.Constants;
import fm.doe.national.core.utils.LifecycleListener;
import fm.doe.national.remote_storage.data.storage.RemoteStorage;
import fm.doe.national.remote_storage.data.uploader.RemoteUploader;
import fm.doe.national.remote_storage.ui.auth.GoogleAuthActivity;
import fm.doe.national.remote_storage.ui.default_storage.DefaultStorageActivity;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.CompletableSubject;
import io.reactivex.subjects.SingleSubject;

public final class RemoteStorageAccessorImpl implements RemoteStorageAccessor {

    private static final long EMPTY_EMIT_DELAY_MS = 500;

    private final LifecycleListener lifecycleListener;
    private final RemoteUploader uploader;
    private final RemoteStorage remoteStorage;
    private final FirebaseAuth firebaseAuth;

    private SingleSubject<String> contentSubject;
    private CompletableSubject authSubject;

    public RemoteStorageAccessorImpl(LifecycleListener lifecycleListener, RemoteUploader uploader, RemoteStorage remoteStorage) {
        this.lifecycleListener = lifecycleListener;
        this.uploader = uploader;
        this.remoteStorage = remoteStorage;
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public Completable signIn() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser == null) {
            authSubject = CompletableSubject.create();
            return Completable.fromAction(() -> {
                Activity currentActivity = lifecycleListener.getCurrentActivity();

                if (currentActivity == null) {
                    authSubject.onComplete();
                    return;
                }

                currentActivity.startActivity(GoogleAuthActivity.createIntent(currentActivity));
            }).andThen(authSubject);
        } else {
            return Completable.complete();
        }
    }

    @Override
    public void signOut() {
        firebaseAuth.signOut();
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
        scheduleEmptyEmit();
        return contentSubject;
    }

    @Override
    public void onContentReceived(String content) {
        if (contentSubject != null) {
            contentSubject.onSuccess(content);
            contentSubject = null;
        }
    }

    @Override
    public void onGoogleSignInAccountReceived(@Nullable GoogleSignInAccount account) {
        UnaryFunction<AuthResult> successHandler = r -> {
            if (authSubject != null) {
                authSubject.onComplete();
                authSubject = null;
            }
        };
        UnaryFunction<Throwable> failureHandler = t -> {
            if (authSubject != null) {
                authSubject.onError(t);
                authSubject = null;
            }
        };

        if (account == null) {
            failureHandler.apply(new AuthenticationException(Constants.Errors.AUTH_FAILED));
        }

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        successHandler.apply(task.getResult());
                    } else {
                        failureHandler.apply(new AuthenticationException(Constants.Errors.AUTH_FAILED));
                    }
                })
                .addOnFailureListener(failureHandler::apply);
    }

    private interface UnaryFunction<T>  {
        void apply(T obj);
    }
}
