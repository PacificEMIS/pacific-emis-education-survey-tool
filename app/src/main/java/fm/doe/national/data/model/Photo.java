package fm.doe.national.data.model;

import androidx.annotation.Nullable;

public interface Photo extends IdentifiedObject {

    @Nullable
    String getLocalPath();

    @Nullable
    String getRemotePath();

}
