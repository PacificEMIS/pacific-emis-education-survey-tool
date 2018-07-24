package fm.doe.national.data_source.static_source.models;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import fm.doe.national.models.survey.School;

public class StaticSchool implements School {

    @Expose
    @SerializedName("schNo")
    private String id;
    @Expose
    @SerializedName("name")
    private String name;

    @Override
    public String getId() {
        return id;
    }

    @NonNull
    @Override
    public String getName() {
        return name;
    }

}
