package fm.doe.national.data.cloud.dropbox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.android.Auth;
import com.dropbox.core.http.OkHttp3Requestor;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.DeletedMetadata;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.files.WriteMode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.data.cloud.CloudAccessor;
import fm.doe.national.data.cloud.CloudPreferences;
import fm.doe.national.ui.screens.cloud.DropboxActivity;
import fm.doe.national.ui.screens.cloud.DropboxView;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.CompletableSubject;
import io.reactivex.subjects.SingleSubject;

public class DropboxCloudAccessor implements CloudAccessor {

    private Context context;
    private CompletableSubject authCompletable;
    private SingleSubject<String> importSingle;
    private CompletableSubject folderPickCompletable;
    private DbxClientV2 dropboxClient;

    private final CloudPreferences cloudPreferences = MicronesiaApplication.getAppComponent().getDropboxCloudPreferences();

    @Nullable
    private String exportFolderPath = cloudPreferences.getExportFolder();


    public DropboxCloudAccessor(Context context) {
        this.context = context;
        if (hasAuthToken()) initDropbox();
    }

    @Override
    public Single<String> importContentFromCloud() {
        importSingle = SingleSubject.create();
        Single<String> launchActivityForContent =
                Completable.fromSingle(
                        createFolderTree()
                                .subscribeOn(Schedulers.io())
                                .doOnSuccess(root -> startPickerActivity(DropboxView.Action.PICK_FILE, root)))
                        .andThen(importSingle);
        if (hasAuthToken()) {
            return launchActivityForContent;
        } else {
            return auth().andThen(launchActivityForContent);
        }
    }

    @Override
    public Completable exportContentToCloud(@NonNull String content, @NonNull String filename) { // TODO
        Completable upload = Completable.fromAction(() -> {
            if (exportFolderPath == null) throw new IllegalStateException("exportFolderPath is null");
            dropboxClient.files()
                    .uploadBuilder(exportFolderPath + "/" + filename)
                    .withMode(WriteMode.OVERWRITE)
                    .uploadAndFinish(new ByteArrayInputStream(content.getBytes()));
        }).subscribeOn(Schedulers.io());
        if (hasAuthToken()) {
            return upload;
        } else {
            return auth().andThen(upload);
        }
    }

    @Override
    public Completable auth() {
        authCompletable = CompletableSubject.create();
        return Completable
                .fromAction(() -> startTransparentActivity(DropboxView.Action.AUTH))
                .andThen(authCompletable);
    }

    @Override
    public Completable selectExportFolder() {
        folderPickCompletable = CompletableSubject.create();
        Completable pickFolder =
                Completable.fromSingle(
                        createFolderTree()
                                .subscribeOn(Schedulers.io())
                                .doOnSuccess(root -> startPickerActivity(DropboxView.Action.PICK_FOLDER, root)))
                        .andThen(folderPickCompletable);
        if (hasAuthToken()) {
            return pickFolder;
        } else {
            return auth().andThen(pickFolder);
        }
    }

    public void onAuthActionComplete() {
        initDropbox();
        authCompletable.onComplete();
    }

    public boolean isSuccessfulAuth() {
        return Auth.getOAuth2Token() != null;
    }

    public void onPickerSuccess(@NonNull BrowsingTreeObject object) {
        if (object.isDirectory()) {
            onFolderPicked(object);
        } else {
            onFilePicked(object);
        }
    }

    private void onFilePicked(@NonNull BrowsingTreeObject object) {
        Single.fromCallable(() -> {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            dropboxClient.files()
                    .download(object.getPath())
                    .download(outputStream);
            return new String(outputStream.toByteArray());
        })
                .subscribeOn(Schedulers.io())
                .doOnSuccess(importSingle::onSuccess)
                .doOnError(importSingle::onError)
                .subscribe();
    }

    private void onFolderPicked(@NonNull BrowsingTreeObject object) {
        exportFolderPath = object.getPath();
        cloudPreferences.saveExportFolder(exportFolderPath);
        folderPickCompletable.onComplete();
    }

    public void onActionFailure(Throwable throwable) {
        if (authCompletable != null) authCompletable.onError(throwable);
        if (importSingle != null) importSingle.onError(throwable);
        if (folderPickCompletable != null) folderPickCompletable.onError(throwable);
    }

    private boolean hasAuthToken() {
        return cloudPreferences.getAccessToken() != null;
    }

    private void initDropbox() {
        String accessToken = cloudPreferences.getAccessToken();
        if (accessToken == null) {
            accessToken = Auth.getOAuth2Token();
            if (accessToken != null) {
                cloudPreferences.saveAccessToken(accessToken);
                initClient(accessToken);
            }
        } else {
            initClient(accessToken);
        }
    }

    private void initClient(@NonNull String accessToken) {
        DbxRequestConfig requestConfig = DbxRequestConfig.newBuilder(context.getApplicationInfo().name)
                .withHttpRequestor(new OkHttp3Requestor(OkHttp3Requestor.defaultOkHttpClient()))
                .build();
        dropboxClient = new DbxClientV2(requestConfig, accessToken);
    }

    @NonNull
    private Single<BrowsingTreeObject> createFolderTree() {
        return Single.fromCallable(() -> {
            if (!hasAuthToken() || dropboxClient == null) throw new IllegalStateException("Not authorised");

            BrowsingTreeObject root = new BrowsingTreeObject();
            root.setDirectory(true);
            populateFolder(root);
            return root;
        });
    }

    private void populateFolder(BrowsingTreeObject folder) {
        ListFolderResult listFolderResult;
        try {
            listFolderResult = dropboxClient.files().listFolder(folder.getPath());

            while (true) {
                for (Metadata md : listFolderResult.getEntries()) {
                    if (md instanceof DeletedMetadata) break;
                    BrowsingTreeObject child = new BrowsingTreeObject();
                    child.setParent(folder);
                    child.setName(md.getName());
                    child.setPath(md.getPathLower());

                    boolean isFolder = md instanceof FolderMetadata;
                    child.setDirectory(isFolder);
                    if (isFolder) populateFolder(child);
                    folder.addChild(child);
                }

                // for pagination (always enabled)
                if (!listFolderResult.getHasMore()) break;
                listFolderResult = dropboxClient.files().listFolderContinue(listFolderResult.getCursor());
            }
        } catch (DbxException ex) {
            onActionFailure(ex);
        }
    }

    private void startPickerActivity(DropboxView.Action action, @NonNull BrowsingTreeObject treeRoot) {
        startActivityAction(action, treeRoot);
    }

    private void startTransparentActivity(DropboxView.Action action) {
        startActivityAction(action, null);
    }

    private void startActivityAction(DropboxView.Action action, @Nullable BrowsingTreeObject treeRoot) {
        Activity activity = ((MicronesiaApplication) context).getCurrentActivity();
        if (activity != null) {
            Intent intent = treeRoot != null ?
                    DropboxActivity.createPickerIntent(activity, action, treeRoot)
                    : DropboxActivity.createIntent(activity, action);
            activity.startActivity(intent);
        } else {
            onActionFailure(new IllegalStateException("No activities running"));
        }
    }
}
