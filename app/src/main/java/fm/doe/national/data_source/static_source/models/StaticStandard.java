package fm.doe.national.data_source.static_source.models;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import fm.doe.national.models.survey.Standard;

public class StaticStandard implements Standard {

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("criteria")
    private List<StaticCriteria> list;

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public long getGroupStandardId() {
        return 0;
    }

    @NonNull
    @Override
    public String getName() {
        return name;
    }

    public List<StaticCriteria> getList() {
        return list;
    }
}
