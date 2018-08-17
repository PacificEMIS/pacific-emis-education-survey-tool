package fm.doe.national.data.data_source.models.serializable;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.Collection;
import java.util.List;

import fm.doe.national.data.data_source.models.GroupStandard;
import fm.doe.national.data.data_source.models.Standard;

public class SerializableGroupStandard implements GroupStandard {

    @Nullable
    @SerializedName("standard")
    private Collection<SerializableStandard> standards;

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
