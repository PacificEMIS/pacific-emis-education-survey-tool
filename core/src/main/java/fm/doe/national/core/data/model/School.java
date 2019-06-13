package fm.doe.national.core.data.model;

import androidx.annotation.NonNull;

import java.io.Serializable;

import fm.doe.national.core.preferences.entities.AppRegion;

public interface School extends Serializable {

    @NonNull
    String getName();

    @NonNull
    String getId();

    @NonNull
    AppRegion getAppRegion();

}
