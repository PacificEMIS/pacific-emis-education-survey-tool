package org.pacific_emis.surveys.remote_data.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Teachers {
    @SerializedName("ResultSet")
    public List<Teacher> teachers;

    @SerializedName("NumResults")
    public int fullAmountOfTeachers;

    @SerializedName("FirstRec")
    public int numberOfFirstRecordOnPage;

    @SerializedName("LastRec")
    public int numberOfLastRecordOnPage;

    @SerializedName("LastPage")
    public int numberOfLastPage;
}
