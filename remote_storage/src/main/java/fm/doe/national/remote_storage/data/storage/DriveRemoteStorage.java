package fm.doe.national.remote_storage.data.storage;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.api.services.drive.Drive;
import com.omega_r.libs.omegatypes.Text;

import java.lang.ref.WeakReference;
import java.util.List;

import fm.doe.national.core.preferences.entities.AppRegion;
import fm.doe.national.core.preferences.entities.OperatingMode;
import fm.doe.national.remote_storage.data.model.GoogleDriveFileHolder;
import io.reactivex.Completable;
import io.reactivex.Single;

public final class DriveRemoteStorage implements RemoteStorage {

    private final WeakReference<Context> contextWeakReference;

    private DriveServiceHelper driveServiceHelper;

    public DriveRemoteStorage(Context appContext) {
        this.contextWeakReference = new WeakReference<>(appContext);
    }

    public void init(Drive driveService) {
        driveServiceHelper = new DriveServiceHelper(driveService);
    }

    @Override
    public Single<List<GoogleDriveFileHolder>> requestStorageFiles(String parentFolderId) {
        return driveServiceHelper.queryFiles(parentFolderId);
    }

    @Override
    public Completable uploadContent(String content, String filename, AppRegion appRegion, OperatingMode operatingMode) {
        return driveServiceHelper.createFolderIfNotExist(unwrap(operatingMode.getName()), null)
                .flatMap(opModeFolderId -> driveServiceHelper.createFolderIfNotExist(unwrap(appRegion.getName()), opModeFolderId))
                .flatMap(regionFolderId -> driveServiceHelper.createOrUpdateFile(filename, content, regionFolderId))
                .ignoreElement();
    }

    @Override
    public Single<String> loadContent(String fileId) {
        return driveServiceHelper.readFile(fileId);
    }

    private String unwrap(@NonNull Text text) {
        return text.getString(contextWeakReference.get());
    }

    @Override
    public Completable delete(String fileId) {
        return driveServiceHelper.delete(fileId);
    }
}
