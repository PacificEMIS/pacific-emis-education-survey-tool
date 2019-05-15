package fm.doe.national.data.model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public interface School extends Serializable {

    @NonNull
    String getName();

    @NonNull
    String getId();

}
