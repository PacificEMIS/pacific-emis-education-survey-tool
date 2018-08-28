package fm.doe.national.data.cloud;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface CloudPreferences {

    @Nullable
    String getExportFolder();

    void saveExportFolder(@NonNull String folder);

    @Nullable
    String getAccessToken();

    void saveAccessToken(@NonNull String accessToken);

}
