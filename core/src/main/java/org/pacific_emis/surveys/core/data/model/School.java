package org.pacific_emis.surveys.core.data.model;

import androidx.annotation.NonNull;

import java.io.Serializable;

import org.pacific_emis.surveys.core.preferences.entities.AppRegion;

public interface School extends Serializable {

    @NonNull
    String getName();

    @NonNull
    String getId();

    @NonNull
    AppRegion getAppRegion();

}
