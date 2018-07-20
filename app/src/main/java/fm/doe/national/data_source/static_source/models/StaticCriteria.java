package fm.doe.national.data_source.static_source.models;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import fm.doe.national.models.survey.Criteria;

public class StaticCriteria implements Criteria {

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("weightage")
    private Integer weightage;

    @Expose
    @SerializedName("subcriteria")
    private List<StaticSubCriteria> list;

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public long getStandardId() {
        return 0;
    }

    @NonNull
    @Override
    public String getName() {
        return name;
    }

    public Integer getWeightage() {
        return weightage;
    }

    public List<StaticSubCriteria> getList() {
        return list;
    }
}
