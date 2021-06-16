package org.pacific_emis.surveys.remote_data.models;

import com.google.gson.annotations.SerializedName;

public class Teacher {
    @SerializedName("tID")
    public int teacherId;

    @SerializedName("tFullName")
    public String name;
}
