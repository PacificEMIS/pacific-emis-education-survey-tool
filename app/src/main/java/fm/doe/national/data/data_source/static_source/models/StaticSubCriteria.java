package fm.doe.national.data.data_source.static_source.models;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import fm.doe.national.models.survey.SubCriteria;

public class StaticSubCriteria implements SubCriteria {

    @Expose
    @SerializedName("name")
    private String name;


    @Override
    public long getId() {
        return 0;
    }

    @Override
    public long getCriteriaId() {
        return 0;
    }

    @NonNull
    @Override
    public String getName() {
        return name;
    }

}
