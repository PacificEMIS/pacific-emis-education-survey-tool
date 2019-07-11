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
    default Image getImage() {
        String localPath = getLocalPath();
        String remotePath = getRemotePath();

        if (localPath != null && new File(localPath).exists()) {
            return UrlImageExtensionsKt.from(Image.Companion, localPath);
        } else if (remotePath != null) {
            return UrlImageExtensionsKt.from(Image.Companion, remotePath);
        }

        return null;
    }

}
