package fm.doe.national.core.data.model;

import androidx.annotation.Nullable;

public interface Photo extends IdentifiedObject {

    @Nullable
    String getLocalPath();

    @Nullable
    String getRemotePath();

}
