package fm.doe.national.data_source.static_source.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fm.doe.national.models.survey.GroupStandard;
import fm.doe.national.models.survey.Standard;

public class StaticGroupStandard implements GroupStandard {

    @Expose
    @Nullable
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("standard")
    private List<StaticStandard> list;

    @Override
    public long getId() {
        return 0;
    }

    @NonNull
    @Override
    public String getName() {
        if (name != null) return name;

        List<Standard> standards = getStandards();
        if (standards.isEmpty()) return "";

        return standards.get(0).getName();
    }

    @Override
    public List<Standard> getStandards() {
        return list == null ? Collections.EMPTY_LIST : new ArrayList<>(list);
    }
}
