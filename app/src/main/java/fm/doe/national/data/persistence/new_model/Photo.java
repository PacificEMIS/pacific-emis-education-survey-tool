package fm.doe.national.data.persistence.new_model;

import androidx.annotation.Nullable;

public interface Photo extends IdentifiedObject {

    @Nullable
    String getLocalPath();

    void setLocalPath(@Nullable String path);

    @Nullable
    String getRemotePath();

    void setRemotePath(@Nullable String remotePath);

}
