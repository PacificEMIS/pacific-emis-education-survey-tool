package fm.doe.national.data.cloud.drive;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.data.cloud.CloudPreferences;

public class DriveCloudPreferences implements CloudPreferences {
    private static final String PREFS_KEY_DRIVE_FOLDER = "PREFS_KEY_DRIVE_FOLDER";

    private final SharedPreferences sharedPreferences = MicronesiaApplication.getAppComponent().getSharedPreferences();

    @Nullable
    @Override
    public String getExportFolder() {
        return sharedPreferences.getString(PREFS_KEY_DRIVE_FOLDER, null);
    }

    @Override
    public void setExportFolder(@NonNull String folder) {
        sharedPreferences.edit().putString(PREFS_KEY_DRIVE_FOLDER, folder).apply();
    }

    @Nullable
    @Override
    public String getAccessToken() {
        return null;
    }

    @Override
    public void setAccessToken(@NonNull String accessToken) {
        // nothing
    }
}
