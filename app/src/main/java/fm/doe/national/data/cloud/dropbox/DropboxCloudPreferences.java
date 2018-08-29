package fm.doe.national.data.cloud.dropbox;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.data.cloud.CloudPreferences;

public class DropboxCloudPreferences implements CloudPreferences {

    private static final String PREFS_KEY_DROPBOX_FOLDER = "PREFS_KEY_DROPBOX_FOLDER";
    private static final String PREFS_KEY_TOKEN = "PREFS_KEY_TOKEN";

    private final SharedPreferences sharedPreferences = MicronesiaApplication.getAppComponent().getSharedPreferences();

    @Nullable
    @Override
    public String getExportFolder() {
        return sharedPreferences.getString(PREFS_KEY_DROPBOX_FOLDER, null);
    }

    @Override
    public void setExportFolder(@NonNull String folder) {
        sharedPreferences.edit().putString(PREFS_KEY_DROPBOX_FOLDER, folder).apply();
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
