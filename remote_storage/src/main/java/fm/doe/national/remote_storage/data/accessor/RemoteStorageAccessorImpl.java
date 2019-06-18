package fm.doe.national.remote_storage.data.accessor;

import android.app.Activity;

import java.util.concurrent.TimeUnit;

import fm.doe.national.core.utils.LifecycleListener;
import fm.doe.national.remote_storage.data.storage.RemoteStorage;
import fm.doe.national.remote_storage.data.uploader.RemoteUploader;
import fm.doe.national.remote_storage.ui.default_storage.DefaultStorageActivity;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.SingleSubject;

public final class RemoteStorageAccessorImpl implements RemoteStorageAccessor {

    private static final long EMPTY_EMIT_DELAY_MS = 500;

    private final LifecycleListener lifecycleListener;
    private final RemoteUploader uploader;
    private final RemoteStorage remoteStorage;

    private SingleSubject<String> contentSubject;

    public RemoteStorageAccessorImpl(LifecycleListener lifecycleListener, RemoteUploader uploader, RemoteStorage remoteStorage) {
        this.lifecycleListener = lifecycleListener;
        this.uploader = uploader;
        this.remoteStorage = remoteStorage;
    }

    @Override
    public void auth() {

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
}
