package org.pacific_emis.surveys.core.data.remote_data_source.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.pacific_emis.surveys.core.preferences.entities.AppRegion;

public class School implements org.pacific_emis.surveys.core.data.model.School {

    @SerializedName("C")
    String code;

    @SerializedName("N")
    String name;

    @Expose(serialize = false, deserialize = false)
    public AppRegion appRegion;

    @NonNull
    @Override
    public String getName() {
        return name;
    }

    @NonNull
    @Override
    public String getId() {
        return code;
    }

    @NonNull
    @Override
    public AppRegion getAppRegion() {
        return appRegion;
    }
}
