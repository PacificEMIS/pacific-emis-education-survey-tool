package fm.doe.national.remote_storage.data.storage;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.omega_r.libs.omegatypes.Text;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import fm.doe.national.core.preferences.GlobalPreferences;
import fm.doe.national.core.preferences.entities.AppRegion;
import fm.doe.national.remote_storage.BuildConfig;
import fm.doe.national.remote_storage.R;
import fm.doe.national.remote_storage.data.model.GoogleDriveFileHolder;
import io.reactivex.Completable;
import io.reactivex.Single;

public final class DriveRemoteStorage implements RemoteStorage {

    private static final Collection<String> sDriveScopes = Arrays.asList(DriveScopes.DRIVE_FILE, DriveScopes.DRIVE_METADATA);
    private static final HttpTransport sTransport = AndroidHttp.newCompatibleTransport();
    private static final GsonFactory sGsonFactory = new GsonFactory();

    private final WeakReference<Context> contextWeakReference;
    private final GlobalPreferences globalPreferences;

    private DriveServiceHelper driveServiceHelper;

    public DriveRemoteStorage(Context appContext, GlobalPreferences globalPreferences) {
        this.contextWeakReference = new WeakReference<>(appContext);
        this.globalPreferences = globalPreferences;
        init();
    }

    public void init() {
        try {
            GoogleCredential credential = GoogleCredential.fromStream(
                    contextWeakReference.get().getAssets().open(getCredentialsFileName()),
                    sTransport,
                    sGsonFactory)
                    .createScoped(sDriveScopes);
            Drive drive = new Drive.Builder(sTransport, sGsonFactory, credential)
                    .setApplicationName(contextWeakReference.get().getString(R.string.app_name))
                    .build();
            driveServiceHelper = new DriveServiceHelper(drive);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getCredentialsFileName() {
        switch (globalPreferences.getOperatingMode()) {
            case DEV:
                return BuildConfig.CREDENTIALS_DEV;
            case PROD:
                return BuildConfig.CREDENTIALS_PROD;
        }
        return BuildConfig.CREDENTIALS_DEV;
    }

    @Override
    public Single<List<GoogleDriveFileHolder>> requestStorageFiles(String parentFolderId) {
        return driveServiceHelper.queryFiles(parentFolderId);
    }

    @Override
    public Completable uploadContent(String content, String filename, AppRegion appRegion) {
        return driveServiceHelper.createFolderIfNotExist(unwrap(appRegion.getName()), null)
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
