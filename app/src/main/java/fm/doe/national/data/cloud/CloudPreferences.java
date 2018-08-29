package fm.doe.national.data.cloud;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface CloudPreferences {

    @Nullable
    String getExportFolder();

    void setExportFolder(@NonNull String folder);

    @Nullable
    String getAccessToken();

    void setAccessToken(@NonNull String accessToken);

}
