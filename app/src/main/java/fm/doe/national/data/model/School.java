package fm.doe.national.data.model;

import androidx.annotation.NonNull;

public interface School extends IdentifiedObject {

    @NonNull
    String getName();

    @NonNull
    String getSuffix();

}
