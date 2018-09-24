package fm.doe.national.data.cloud;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface CloudPreferences {

    @Nullable
    String getExportFolder();
    @Nullable
    String getExportFolderPath();

    void setExportFolder(@NonNull String folder);

    void setExportFolderPath(@NonNull String folderPath);

    @Nullable
    String getAccessToken();

    void setAccessToken(@NonNull String accessToken);

}
