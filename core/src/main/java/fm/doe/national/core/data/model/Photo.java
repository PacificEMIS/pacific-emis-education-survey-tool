package fm.doe.national.core.data.model;

import androidx.annotation.Nullable;

import com.omega_r.libs.omegatypes.Image;
import com.omega_r.libs.omegatypes.UrlImageExtensionsKt;

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
        } else if (remotePath != null) {
            return remotePath;
        }

        return null;
    }

    @Nullable
    default Image getImage() {
        String path = getUsablePath();

        if (path != null) {
            return UrlImageExtensionsKt.from(Image.Companion, path);
        }

        return null;
    }

}
