package fm.doe.national.remote_storage.data.accessor;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

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
import fm.doe.national.remote_storage.data.storage.RemoteStorage;
import fm.doe.national.remote_storage.data.uploader.RemoteUploader;
import fm.doe.national.remote_storage.ui.default_storage.DefaultStorageActivity;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.CompletableSubject;
import io.reactivex.subjects.SingleSubject;

public final class RemoteStorageAccessorImpl implements RemoteStorageAccessor {

    private static final long EMPTY_EMIT_DELAY_MS = 500;

    private final LifecycleListener lifecycleListener;
    private final RemoteUploader uploader;
    private final RemoteStorage remoteStorage;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private SingleSubject<String> contentSubject;
    private CompletableSubject authSubject;
    private GoogleSignInAccount account;
    private DriveServiceHelper driveServiceHelper;

    public RemoteStorageAccessorImpl(LifecycleListener lifecycleListener,
                                     Context appContext,
                                     RemoteUploader uploader,
                                     RemoteStorage remoteStorage) {
        this.lifecycleListener = lifecycleListener;
        this.uploader = uploader;
        this.remoteStorage = remoteStorage;
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
            driveServiceHelper = new DriveServiceHelper(drive);

            compositeDisposable.add(driveServiceHelper.queryFiles(null)
                    .flatMap(files -> driveServiceHelper.createOrUpdateFile("example.xml", "other content", "1POKajwPmPflbTz9nuVf3-cCiuJsZrKp2"))
                    .flatMap(fileId -> driveServiceHelper.queryFiles("1POKajwPmPflbTz9nuVf3-cCiuJsZrKp2"))
                    .subscribeOn(Schedulers.io())
                    .subscribe(fileId -> {
                        Log.d("RemoteStorageAccessorImpl", "onSuccess");
                    }, Throwable::printStackTrace));


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

        return Completable.fromAction(() -> {
            account = GoogleSignIn.getLastSignedInAccount(currentActivity);
        });
    }

    @Override
    public void signOutAsUser() {
        compositeDisposable.dispose();
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

    private interface UnaryFunction<T> {
        void apply(T obj);
    }
}
