package fm.doe.national.data.cloud.drive;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveClient;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResourceClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.data.cloud.CloudAccessor;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.SingleSubject;


public class DriveCloudAccessor implements CloudAccessor {
    private Context context;

    private SingleSubject<Object> authSingle;
    private SingleSubject<String> importSingle;
    private SingleSubject<Object> exportSingle;

    public DriveCloudAccessor(Context appContext) {
        context = appContext;
    }

    @Override
    public Single<String> importContentFromCloud() {
        importSingle = SingleSubject.create();
        Single<String> importContent =
                Completable
                        .fromAction(() -> startActivityAction(DriveActivity.ACTION_OPEN_FILE))
                        .andThen(importSingle);
        Single<String> resultingSingle = isAuthenticated() ? importContent : auth().andThen(importContent);
        return resultingSingle.subscribeOn(Schedulers.io());
    }

    @Override
    public Completable exportContentToCloud(@NonNull String content) {
        exportSingle = SingleSubject.create();
        Single<Object> exportContent =
                Completable
                        .fromAction(() -> startActivityToUpload(content))
                        .andThen(exportSingle);
        Completable resultingCompletable = isAuthenticated() ?
                Completable.fromSingle(exportContent)
                : Completable.fromSingle(auth().andThen(exportContent));
        return resultingCompletable.subscribeOn(Schedulers.io());
    }

    @Override
    public Completable auth() {
        authSingle = SingleSubject.create();
        startActivityAction(DriveActivity.ACTION_AUTH);
        return Completable.fromSingle(authSingle).subscribeOn(Schedulers.io());
    }

    protected void onAuth() {
        if (authSingle == null) return;

        if (isAuthenticated()) {
            authSingle.onSuccess(new Object());
        } else {
            authSingle.onError(new Exception("Failed to sign in"));
        }
    }

    protected void onFileContentObtained(@NonNull DriveId fileDriveId) {
        if (importSingle == null) return;

        DriveResourceClient resourceClient = getDriveResourceClient();
        if (resourceClient == null) {
            importSingle.onError(new Exception("Drive Resource Client lost"));
            return;
        }

        DriveFile file = fileDriveId.asDriveFile();
        getDriveResourceClient().openFile(file, DriveFile.MODE_READ_ONLY)
                .continueWith(task -> {
                    DriveContents contents = task.getResult();
                    try (BufferedReader reader = new BufferedReader(
                            new InputStreamReader(contents.getInputStream()))) {
                        StringBuilder builder = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            builder.append(line).append("\n");
                        }
                        importSingle.onSuccess(builder.toString());
                    }
                    return resourceClient.discardContents(contents);
                });
    }

    protected void onExport() {
        exportSingle.onSuccess(new Object());
    }

    protected void onActionFailure(Throwable throwable) {
        if (authSingle != null) authSingle.onError(throwable);
        if (importSingle != null) importSingle.onError(throwable);
        if (exportSingle != null) exportSingle.onError(throwable);
    }

    @Nullable
    protected DriveClient getDriveClient() {
        GoogleSignInAccount account = getGoogleAccount();
        if (account == null) return null;
        return Drive.getDriveClient(context, account);
    }

    @Nullable
    protected DriveResourceClient getDriveResourceClient() {
        GoogleSignInAccount account = getGoogleAccount();
        if (account == null) return null;
        return Drive.getDriveResourceClient(context, account);
    }

    private boolean isAuthenticated() {
        return getGoogleAccount() != null;
    }

    @Nullable
    private GoogleSignInAccount getGoogleAccount() {
        return GoogleSignIn.getLastSignedInAccount(context);
    }

    private void startActivityAction(int activityAction) {
        startActivity(DriveActivity.createIntent(context, activityAction));
    }

    private void startActivityToUpload(@NonNull String content) {
        startActivity(DriveActivity.createUploadIntent(context, content));
    }

    private void startActivity(Intent intent) {
        Activity activity = ((MicronesiaApplication)context).getCurrentActivity();
        if (activity != null) {
            activity.startActivity(intent);
        } else {
            onActionFailure(new Exception("No activities running"));
        }
    }
}
