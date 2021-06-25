package org.pacific_emis.surveys.core.data.remote_data_source.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.pacific_emis.surveys.core.preferences.entities.AppRegion;

public class Teacher implements org.pacific_emis.surveys.core.data.model.Teacher {
    @SerializedName("tID")
    int teacherId;

    @SerializedName("tFullName")
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
    public Integer getId() {
        return teacherId;
    }

    @NonNull
    @Override
    public AppRegion getAppRegion() {
        return appRegion;
    }
}
