package org.pacific_emis.surveys.remote_data.models;

import com.google.gson.annotations.SerializedName;

public class Token {
    @SerializedName("access_token")
    public String raw;

    @Override
    public String toString() {
        return "Bearer " + raw;
    }
}
