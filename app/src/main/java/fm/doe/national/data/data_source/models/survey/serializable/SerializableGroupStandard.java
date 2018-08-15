package fm.doe.national.data.data_source.models.survey.serializable;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import fm.doe.national.data.data_source.models.survey.GroupStandard;
import fm.doe.national.data.data_source.models.survey.Standard;

public class SerializableGroupStandard implements GroupStandard {

    @Nullable
    @SerializedName("standard")
    private List<Standard> standards;

    @NonNull
    @Override
    public String getName() {
        return "";
    }

    @Nullable
    public Collection<? extends Standard> getStandards() {
        return standards;
    }
}
