package org.pacific_emis.surveys.core.data.remote_data_source.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.pacific_emis.surveys.core.data.model.abstract_implementations.SubjectImpl;
import org.pacific_emis.surveys.core.preferences.entities.AppRegion;

public class Subject extends SubjectImpl {
    @SerializedName("Code")
    String subjectId;

    @SerializedName("Description")
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
        return subjectId;
    }

    @NonNull
    @Override
    public AppRegion getAppRegion() {
        return appRegion;
    }
}
