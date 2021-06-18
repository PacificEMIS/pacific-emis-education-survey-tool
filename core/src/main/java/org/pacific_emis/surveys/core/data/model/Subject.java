package org.pacific_emis.surveys.core.data.model;

import androidx.annotation.NonNull;

import org.pacific_emis.surveys.core.preferences.entities.AppRegion;

import java.io.Serializable;

public interface Subject extends Serializable {

    @NonNull
    String getName();

    @NonNull
    String getId();

    @NonNull
    AppRegion getAppRegion();

}
