package fm.doe.national.core.data.model;

import androidx.annotation.Nullable;

import com.omega_r.libs.omegatypes.image.Image;
import com.omega_r.libs.omegatypes.image.UrlImage;

import java.io.File;

public interface Photo extends IdentifiedObject {

    @Nullable
    String getLocalPath();

    @Nullable
    String getRemotePath();

    @Nullable
    default String getUsablePath() {
        String localPath = getLocalPath();
        String remotePath = getRemotePath();

        if (localPath != null && new File(localPath).exists()) {
            return localPath;
        } else {
            return remotePath;
        }
    }

    @Nullable
    default Image getImage() {
        String path = getUsablePath();

        if (path != null) {
            return new UrlImage(path);
        }

        return null;
    }

}
