package fm.doe.national.data.cloud.drive;

import android.app.Activity;
import android.content.Context;
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

    public DriveCloudAccessor(Context appContext) {
        context = appContext;
    }

    //region CloudAccessor
    @Override
    public Single<String> importContentFromCloud() {
        importSingle = SingleSubject.create();
        Single<String> importContent =
                Completable.fromRunnable(() -> startActivityAction(DriveActivity.ACTION_AUTH))
                .andThen(Completable.fromRunnable(() -> startActivityAction(DriveActivity.ACTION_OPEN_FILE)))
                .andThen(importSingle);
        Single<String> resultingSingle = isAuthenticated() ? importContent : auth().andThen(importContent);
        return resultingSingle.subscribeOn(Schedulers.io());
    }

    @Override
    public Completable exportContentToCloud(@NonNull String content) {
        return null;
    }

    @Override
    public Completable auth() {
        startActivityAction(DriveActivity.ACTION_AUTH);
        authSingle = SingleSubject.create();
        return Completable.fromSingle(authSingle).subscribeOn(Schedulers.io());
    }

    @Override
    public Type getType() {
        return Type.DRIVE;
    }
    //endregion

    protected void onAuth() {
        if (authSingle == null) return;

        if (isAuthenticated()) {
            authSingle.onSuccess(new Object());
        } else {
            authSingle.onError(new Exception("Failed to sign in"));
        }
    }

    protected void onFileContentObtained(@Nullable DriveId fileDriveId) {
        if (importSingle == null) return;

        if (fileDriveId == null) {
            importSingle.onError(new Exception("Failed to obtain file"));
            return;
        }

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

    @Nullable
    protected GoogleSignInAccount getGoogleAccount() {
        return GoogleSignIn.getLastSignedInAccount(context);
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

    private void startActivityAction(int activityAction) {
        Activity activity = ((MicronesiaApplication)context).getCurrentActivity();
        if (activity != null) {
            activity.startActivity(DriveActivity.createIntent(context, activityAction));
        }
    }
}
