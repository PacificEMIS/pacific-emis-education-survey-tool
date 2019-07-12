package fm.doe.national.remote_storage.data.storage;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.omega_r.libs.omegatypes.Text;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;

import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.data.serialization.SurveySerializer;
import fm.doe.national.core.preferences.GlobalPreferences;
import fm.doe.national.remote_storage.BuildConfig;
import fm.doe.national.remote_storage.R;
import fm.doe.national.remote_storage.data.model.GoogleDriveFileHolder;
import fm.doe.national.remote_storage.data.model.NdoeMetadata;
import fm.doe.national.remote_storage.utils.TextUtil;
import io.reactivex.Completable;
import io.reactivex.Single;

public final class DriveRemoteStorage implements RemoteStorage {

    private static final Collection<String> sDriveScopes = Arrays.asList(DriveScopes.DRIVE_FILE, DriveScopes.DRIVE_METADATA);
    private static final HttpTransport sTransport = AndroidHttp.newCompatibleTransport();
    private static final GsonFactory sGsonFactory = new GsonFactory();
    private final SurveySerializer surveySerializer;

    private final Context appContext;
    private final GlobalPreferences globalPreferences;

    private DriveServiceHelper driveServiceHelper;

    @Nullable
    private GoogleSignInAccount userAccount;

    public DriveRemoteStorage(Context appContext, GlobalPreferences globalPreferences, SurveySerializer surveySerializer) {
        this.appContext = appContext;
        this.globalPreferences = globalPreferences;
        this.surveySerializer = surveySerializer;
        refreshCredentials();
        userAccount = GoogleSignIn.getLastSignedInAccount(appContext);
    }

    @Override
    public void refreshCredentials() {
        try {
            GoogleCredential credential = GoogleCredential.fromStream(
                    appContext.getAssets().open(getCredentialsFileName()),
                    sTransport,
                    sGsonFactory)
                    .createScoped(sDriveScopes);
            Drive drive = new Drive.Builder(sTransport, sGsonFactory, credential)
                    .setApplicationName(appContext.getString(R.string.drive_app_name))
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
            default:
                return BuildConfig.CREDENTIALS_DEV;
        }
    }

    @Override
    public Single<List<GoogleDriveFileHolder>> requestStorageFiles(String parentFolderId) {
        return driveServiceHelper.queryFiles(parentFolderId);
    }

    @Override
    public Completable upload(Survey survey) {
        if (userAccount == null) {
            return Completable.error(new IllegalStateException());
        }

        return driveServiceHelper.createFolderIfNotExist(unwrap(survey.getAppRegion().getName()), null)
                .flatMap(regionFolderId -> driveServiceHelper.createOrUpdateFile(
                        TextUtil.createSurveyFileName(survey),
                        surveySerializer.serialize(survey),
                        new NdoeMetadata(survey, userAccount.getEmail()),
                        regionFolderId))
                .ignoreElement();
    }

    @Override
    public Single<String> loadContent(String fileId) {
        return driveServiceHelper.readFile(fileId);
    }

    private String unwrap(@NonNull Text text) {
        return text.getString(appContext);
    }

    @Override
    public Completable delete(String fileId) {
        return driveServiceHelper.delete(fileId);
    }

    @Nullable
    @Override
    public GoogleSignInAccount getUserAccount() {
        return userAccount;
    }

    @Override
    public void setUserAccount(@Nullable GoogleSignInAccount account) {
        this.userAccount = account;
    }
}
