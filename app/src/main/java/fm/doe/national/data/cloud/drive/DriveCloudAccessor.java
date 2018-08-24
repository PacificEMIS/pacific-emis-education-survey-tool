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
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.tasks.Task;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.data.cloud.CloudAccessor;
import fm.doe.national.data.cloud.exceptions.AuthenticationException;
import fm.doe.national.data.cloud.exceptions.FileExportException;
import fm.doe.national.data.cloud.exceptions.FileImportException;
import fm.doe.national.ui.screens.cloud.DriveActivity;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.subjects.SingleSubject;


public class DriveCloudAccessor implements CloudAccessor {
    private Context context;

    @Nullable
    private DriveId exportFolderId;

    private SingleSubject<Object> authSingle;
    private SingleSubject<String> importSingle;
    private SingleSubject<Object> exportSingle;
    private SingleSubject<Object> pickDirectorySingle;

    @Nullable
    private DriveClient driveClient;

    @Nullable
    private DriveResourceClient driveResourceClient;

    public DriveCloudAccessor(Context appContext) {
        context = appContext;
        initDriveClients();
    }

    @Override
    public Single<String> importContentFromCloud() {
        importSingle = SingleSubject.create();
        Single<String> importContent =
                Completable
                        .fromAction(() -> startActivityAction(DriveActivity.ACTION_OPEN_FILE))
                        .andThen(importSingle);
        return isAuthenticated() ? importContent : auth().andThen(importContent);
    }

    @Override
    public Completable exportContentToCloud(@NonNull String content) { // TODO: wrong behaviour
        exportSingle = SingleSubject.create();
        Single<Object> exportContent =
                Completable
                        .fromAction(() -> startActivityToUpload(content))
                        .andThen(exportSingle);
        return isAuthenticated() ?
                Completable.fromSingle(exportContent)
                : Completable.fromSingle(auth().andThen(exportContent));
    }

    @Override
    public Completable auth() {
        authSingle = SingleSubject.create();
        return Completable
                .fromAction(() -> startActivityAction(DriveActivity.ACTION_AUTH))
                .andThen(Completable.fromSingle(authSingle));
    }

    @Override
    public Completable selectExportFolder() {
        pickDirectorySingle = SingleSubject.create();
        Single<Object> pickDirectory = Completable.
                fromAction(() -> startActivityAction(DriveActivity.ACTION_PICK_FOLDER))
                .andThen(pickDirectorySingle);
        Single<Object> resultingSingle = isAuthenticated() ? pickDirectory : auth().andThen(pickDirectory);
        return Completable.fromSingle(resultingSingle);
    }

    public void onAuth() {
        if (authSingle == null) return;

        if (isAuthenticated()) {
            initDriveClients();
            authSingle.onSuccess(new Object());
        } else {
            authSingle.onError(new AuthenticationException("User not signed id"));
        }
    }

    public void onFileContentObtained(@NonNull DriveId fileDriveId) {
        if (importSingle == null) return;

        if (driveResourceClient == null) {
            importSingle.onError(new FileImportException("Drive Resource Client lost"));
            return;
        }

        DriveFile file = fileDriveId.asDriveFile();
        driveResourceClient.openFile(file, DriveFile.MODE_READ_ONLY)
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
                    return driveResourceClient.discardContents(contents);
                });
    }

    public void onExport() {
        if (exportSingle == null) return;

        exportSingle.onSuccess(new Object());
    }

    public void onFolderPicked(@NonNull DriveId driveId) {
        if (pickDirectorySingle == null) return;

        exportFolderId = driveId;
        //TODO: save to sharedPrefs
        pickDirectorySingle.onSuccess(driveId.encodeToString());
    }

    public void onActionFailure(Throwable throwable) {
        if (authSingle != null) authSingle.onError(throwable);
        if (importSingle != null) importSingle.onError(throwable);
        if (exportSingle != null) exportSingle.onError(throwable);
        if (pickDirectorySingle != null) pickDirectorySingle.onError(throwable);
    }

    @Nullable
    public DriveClient getDriveClient() {
        return driveClient;
    }

    @Nullable
    public DriveResourceClient getDriveResourceClient() {
        return driveResourceClient;
    }

    private void initDriveClients() {
        GoogleSignInAccount account = getGoogleAccount();
        if (account == null) return;

        driveClient = Drive.getDriveClient(context, account);
        driveResourceClient = Drive.getDriveResourceClient(context, account);
        // TODO: init exportFolderId from sharedPref
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
            onActionFailure(new IllegalStateException("no activities running"));
        }
    }

    private void createFile(String content, String filename, DriveId folderDriveId) {
        if (driveResourceClient == null) {
            onActionFailure(new FileExportException("DriveResourceClient is null"));
            return;
        }

        Task<DriveContents> createContentsTask = driveResourceClient.createContents();
        createContentsTask.continueWithTask(task -> {
                    DriveContents contents = createContentsTask.getResult();
                    OutputStream outputStream = contents.getOutputStream();
                    try (Writer writer = new OutputStreamWriter(outputStream)) {
                        writer.write(content);
                    }

                    MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                            .setTitle(filename)
                            .setMimeType("text/plain")
                            .build();

                    return driveResourceClient.createFile(folderDriveId.asDriveFolder(), changeSet, contents);
                })
                .addOnSuccessListener(driveFile -> {})
                .addOnFailureListener(e -> {});
    }
}
