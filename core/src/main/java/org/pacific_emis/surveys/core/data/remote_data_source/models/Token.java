package org.pacific_emis.surveys.core.data.remote_data_source.models;

import com.google.gson.annotations.SerializedName;

public class Token {
    @SerializedName("access_token")
    public String raw;

    @Override
    public String toString() {
        return "Bearer " + raw;
    }
}
