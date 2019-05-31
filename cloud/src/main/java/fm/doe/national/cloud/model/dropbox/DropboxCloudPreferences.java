package fm.doe.national.cloud.model.dropbox;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import fm.doe.national.cloud.model.CloudPreferences;

/**
 * @Deprecated
 * Not needed in current release
 */
@Deprecated
public class DropboxCloudPreferences implements CloudPreferences {

    private static final String PREFS_KEY_DROPBOX_FOLDER = "PREFS_KEY_DROPBOX_FOLDER";
    private static final String PREFS_KEY_DROPBOX_FOLDER_PATH = "PREFS_KEY_DROPBOX_FOLDER_PATH";
    private static final String PREFS_KEY_TOKEN = "PREFS_KEY_TOKEN";

    private final SharedPreferences sharedPreferences;

    public DropboxCloudPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Nullable
    @Override
    public String getExportFolder() {
        return sharedPreferences.getString(PREFS_KEY_DROPBOX_FOLDER, null);
    }

    @Nullable
    @Override
    public String getExportFolderPath() {
        return sharedPreferences.getString(PREFS_KEY_DROPBOX_FOLDER_PATH, null);
    }

    @Override
    public void setExportFolder(@NonNull String folder) {
        sharedPreferences.edit().putString(PREFS_KEY_DROPBOX_FOLDER, folder).apply();
    }

    @Override
    public void setExportFolderPath(@NonNull String folderPath) {
        sharedPreferences.edit().putString(PREFS_KEY_DROPBOX_FOLDER_PATH, folderPath).apply();
    }

    @Nullable
    @Override
    public String getAccessToken() {
        return sharedPreferences.getString(PREFS_KEY_TOKEN, null);
    }

    @Override
    public void setAccessToken(@NonNull String accessToken) {
        sharedPreferences.edit().putString(PREFS_KEY_TOKEN, accessToken).apply();
    }
}
