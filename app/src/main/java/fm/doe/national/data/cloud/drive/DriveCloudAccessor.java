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
import com.google.android.gms.drive.DriveResource;
import com.google.android.gms.drive.DriveResourceClient;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;
import com.google.android.gms.tasks.Tasks;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.data.cloud.CloudAccessor;
import fm.doe.national.data.cloud.CloudPreferences;
import fm.doe.national.data.cloud.exceptions.AuthenticationException;
import fm.doe.national.data.cloud.exceptions.FileExportException;
import fm.doe.national.data.cloud.exceptions.FileImportException;
import fm.doe.national.data.cloud.exceptions.PickException;
import fm.doe.national.ui.screens.cloud.DriveActivity;
import fm.doe.national.utils.Constants;
import fm.doe.national.utils.LifecycleListener;
import fm.doe.national.utils.StreamUtils;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.CompletableSubject;
import io.reactivex.subjects.SingleSubject;


public class DriveCloudAccessor implements CloudAccessor {

    private final CloudPreferences cloudPreferences = MicronesiaApplication.getAppComponent().getDriveCloudPreferences();
    private final LifecycleListener lifecycleListener = MicronesiaApplication.getAppComponent().getLifecycleListener();
    private final Context context;

    @Nullable
    private DriveId exportFolderId;

    private CompletableSubject authCompletable;
    private SingleSubject<String> importSingle;
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
        return auth().andThen(importContent);
    }

    @Override
    public Completable exportContentToCloud(@NonNull String content, @NonNull String filename) {
        return auth()
                .andThen(Completable.fromAction(() -> writeFile(content, filename, exportFolderId)));
    }

    @Override
    public Completable auth() {
        if (isAuthenticated()) {
            initDriveClients();
            return Completable.complete();
        }

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
                .andThen(pickDirectoryCompletable)
                .andThen(extractExportFolderPath());
        return auth().andThen(pickDirectory);
    }

    @Override
    public String getEmail() {
        return "";
    }

    @Override
    public String getExportPath() {
        String exportFolderPath = cloudPreferences.getExportFolderPath();
        if (exportFolderPath == null) return "";
        return exportFolderPath;
    }

    @Override
    public boolean isInUse() {
        return isAuthenticated();
    }

    public void onAuth() {
        if (authCompletable == null) return;

        if (isAuthenticated()) {
            initDriveClients();
            authCompletable.onComplete();
        } else {
            authCompletable.onError(new AuthenticationException(Constants.Errors.AUTH_FAILED));
        }
        authCompletable = null;
    }

    public void onFileContentObtained(@NonNull DriveId fileDriveId) {
        if (importSingle == null) return;

        if (driveResourceClient == null) {
            importSingle.onError(new FileImportException(Constants.Errors.DRIVE_RESOURCE_CLIENT_IS_NULL));
            importSingle = null;
            return;
        }

        DriveFile file = fileDriveId.asDriveFile();
        driveResourceClient.openFile(file, DriveFile.MODE_READ_ONLY)
                .continueWith(task -> {
                    DriveContents contents = task.getResult();
                    importSingle.onSuccess(StreamUtils.asString(contents.getInputStream()));
                    importSingle = null;
                    return driveResourceClient.discardContents(contents);
                });
    }

    private Completable extractExportFolderPath() {
        return Completable.fromAction(() -> {
            if (driveResourceClient == null) {
                throw new PickException(Constants.Errors.DRIVE_RESOURCE_CLIENT_IS_NULL);
            }

            StringBuilder pathBuilder = new StringBuilder();

            DriveResource driveResource = exportFolderId.asDriveResource();
            Metadata folderMeta = Tasks.await(driveResourceClient.getMetadata(driveResource));

            extractPath(pathBuilder, driveResource);
            pathBuilder.append(folderMeta.getTitle());

            cloudPreferences.setExportFolderPath(pathBuilder.toString());
        }).subscribeOn(Schedulers.io());
    }

    private void extractPath(StringBuilder pathBuilder, DriveResource driveResource) throws PickException, ExecutionException, InterruptedException {
        MetadataBuffer parentsMetaBuffer = Tasks.await(driveResourceClient.listParents(driveResource));

        Metadata parentMeta = parentsMetaBuffer.get(0);
        if (parentMeta != null) {
            extractPath(pathBuilder, parentMeta.getDriveId().asDriveResource());
            pathBuilder.append(parentMeta.getTitle()).append(Constants.SYMBOL_SLASH);
        }
    }

    public void onFolderPicked(@NonNull DriveId driveId) {
        if (pickDirectoryCompletable == null) return;

        exportFolderId = driveId;
        cloudPreferences.setExportFolder(exportFolderId.encodeToString());
        pickDirectoryCompletable.onComplete();
        pickDirectoryCompletable = null;
    }

    public void onActionFailure(Throwable throwable) {
        if (authCompletable != null) {
            authCompletable.onError(throwable);
            authCompletable = null;
        }
        if (importSingle != null) {
            importSingle.onError(throwable);
            importSingle = null;
        }
        if (pickDirectoryCompletable != null) {
            pickDirectoryCompletable.onError(throwable);
            pickDirectoryCompletable = null;
        }
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
        Activity currentActivity = lifecycleListener.getCurrentActivity();
        if (currentActivity != null) {
            currentActivity.startActivity(DriveActivity.createIntent(currentActivity, activityAction));
        } else {
            onActionFailure(new IllegalStateException(Constants.Errors.NO_ACTIVITIES));
        }
    }

    private void writeFile(String content, String filename, @Nullable DriveId folderDriveId)
            throws ExecutionException, InterruptedException, IOException  {
        if (driveResourceClient == null) {
            onActionFailure(new FileExportException(Constants.Errors.DRIVE_RESOURCE_CLIENT_IS_NULL));
            return;
        }

        if (folderDriveId == null) {
            onActionFailure(new FileExportException(Constants.Errors.EXPORT_FOLDER_NOT_SPECIFIED));
            return;
        }

        Query query = new Query.Builder()
                .addFilter(Filters.eq(SearchableField.TITLE, filename))
                .build();
        MetadataBuffer buffer = Tasks.await(driveResourceClient.query(query));
        if (buffer.getCount() == 0) {
            createFile(content, filename, folderDriveId, driveResourceClient);
        } else {
            overwriteFile(buffer.get(0).getDriveId(), content, driveResourceClient);
        }
    }

    private void overwriteFile(DriveId fileId, String content, DriveResourceClient resourceClient)
            throws ExecutionException, InterruptedException, IOException {
        DriveContents driveContents = Tasks.await(resourceClient.openFile(fileId.asDriveFile(), DriveFile.MODE_WRITE_ONLY));
        fillContents(driveContents, content);
        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                .setLastViewedByMeDate(new Date())
                .build();
        Tasks.await(resourceClient.commitContents(driveContents, changeSet));
    }

    private void createFile(String content, String filename, DriveId folderDriveId, DriveResourceClient resourceClient)
            throws ExecutionException, InterruptedException, IOException {
        DriveContents contents = Tasks.await(resourceClient.createContents());
        fillContents(contents, content);
        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                .setTitle(filename)
                .setMimeType(Constants.FILE_MIME_TYPE)
                .build();
        Tasks.await(resourceClient.createFile(folderDriveId.asDriveFolder(), changeSet, contents));
    }

    private void fillContents(DriveContents contents, String content) throws IOException {
        Writer writer = new OutputStreamWriter(contents.getOutputStream());
        writer.write(content);
    }
}
