package fm.doe.national.data.data_source.models.serializable;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.Collection;

import fm.doe.national.data.data_source.models.Criteria;
import fm.doe.national.data.data_source.models.GroupStandard;
import fm.doe.national.data.data_source.models.Standard;

public class SerializableStandard implements Standard {

    private String name;

    @SerializedName("criteria")
    private Collection<SerializableCriteria> criterias;

    @Nullable
    @Override
    public GroupStandard getGroupStandard() {
        return null;
    }

    @NonNull
    @Override
    public String getName() {
        return name;
    }

    @Override
    public Collection<? extends Criteria> getCriterias() {
        return criterias;
    }
}
