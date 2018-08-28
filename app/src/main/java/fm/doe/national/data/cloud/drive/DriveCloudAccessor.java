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
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.tasks.Task;

import java.io.OutputStreamWriter;
import java.io.Writer;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.data.cloud.CloudAccessor;
import fm.doe.national.data.cloud.CloudPreferences;
import fm.doe.national.data.cloud.exceptions.AuthenticationException;
import fm.doe.national.data.cloud.exceptions.FileExportException;
import fm.doe.national.data.cloud.exceptions.FileImportException;
import fm.doe.national.ui.screens.cloud.DriveActivity;
import fm.doe.national.utils.StreamUtils;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.subjects.CompletableSubject;
import io.reactivex.subjects.SingleSubject;


public class DriveCloudAccessor implements CloudAccessor {

    private final CloudPreferences cloudPreferences = MicronesiaApplication.getAppComponent().getDriveCloudPreferences();

    private Context context;

    @Nullable
    private DriveId exportFolderId;

    private CompletableSubject authCompletable;
    private SingleSubject<String> importSingle;
    private CompletableSubject exportCompletable;
    private CompletableSubject pickDirectoryCompletable;

    @Nullable
    private DriveClient driveClient;

    @Nullable
    private DriveResourceClient driveResourceClient;

    public DriveCloudAccessor(Context context) {
        this.context = context;
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
    public Completable exportContentToCloud(@NonNull String content, @NonNull String filename) {
        exportCompletable = CompletableSubject.create();
        Completable exportContent =
                Completable
                        .fromAction(() -> createFile(content, filename, exportFolderId))
                        .andThen(exportCompletable);
        return isAuthenticated() ? exportContent : auth().andThen(exportContent);
    }

    @Override
    public Completable auth() {
        authCompletable = CompletableSubject.create();
        return Completable
                .fromAction(() -> startActivityAction(DriveActivity.ACTION_AUTH))
                .andThen(authCompletable);
    }

    @Override
    public Completable selectExportFolder() {
        pickDirectoryCompletable = CompletableSubject.create();
        Completable pickDirectory = Completable.
                fromAction(() -> startActivityAction(DriveActivity.ACTION_PICK_FOLDER))
                .andThen(pickDirectoryCompletable);
        return isAuthenticated() ? pickDirectory : auth().andThen(pickDirectory);
    }

    public void onAuth() {
        if (authCompletable == null) return;

        if (isAuthenticated()) {
            initDriveClients();
            authCompletable.onComplete();
        } else {
            authCompletable.onError(new AuthenticationException("User not signed id"));
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
                    importSingle.onSuccess(StreamUtils.asString(contents.getInputStream()));
                    return driveResourceClient.discardContents(contents);
                });
    }

    public void onFolderPicked(@NonNull DriveId driveId) {
        if (pickDirectoryCompletable == null) return;

        exportFolderId = driveId;
        cloudPreferences.saveExportFolder(exportFolderId.encodeToString());
        pickDirectoryCompletable.onComplete();
    }

    public void onActionFailure(Throwable throwable) {
        if (authCompletable != null) authCompletable.onError(throwable);
        if (importSingle != null) importSingle.onError(throwable);
        if (exportCompletable != null) exportCompletable.onError(throwable);
        if (pickDirectoryCompletable != null) pickDirectoryCompletable.onError(throwable);
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

        String exportFolder = cloudPreferences.getExportFolder();
        if (exportFolder != null) {
            exportFolderId = DriveId.decodeFromString(exportFolder);
        }
    }

    private boolean isAuthenticated() {
        return getGoogleAccount() != null;
    }

    @Nullable
    private GoogleSignInAccount getGoogleAccount() {
        return GoogleSignIn.getLastSignedInAccount(context);
    }

    private void startActivityAction(int activityAction) {
        Activity currentActivity = ((MicronesiaApplication) context).getCurrentActivity();
        if (currentActivity != null) {
            currentActivity.startActivity(DriveActivity.createIntent(context, activityAction));
        } else {
            onActionFailure(new IllegalStateException("no activities running"));
        }
    }

    private void createFile(String content, String filename, @Nullable DriveId folderDriveId) {
        if (driveResourceClient == null) {
            onActionFailure(new FileExportException("DriveResourceClient is null"));
            return;
        }

        if (folderDriveId == null) {
            onActionFailure(new FileExportException("folder not specified"));
            return;
        }

        Task<DriveContents> createContentsTask = driveResourceClient.createContents();
        createContentsTask.continueWithTask(task -> {
            DriveContents contents = createContentsTask.getResult();
            try (Writer writer = new OutputStreamWriter(contents.getOutputStream())) {
                writer.write(content);
            }

            MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                    .setTitle(filename)
                    .setMimeType("text/plain")
                    .build();

            return driveResourceClient.createFile(folderDriveId.asDriveFolder(), changeSet, contents);
        })
                .addOnSuccessListener(driveFile -> exportCompletable.onComplete())
                .addOnFailureListener(throwable -> exportCompletable.onError(throwable));
    }
}
