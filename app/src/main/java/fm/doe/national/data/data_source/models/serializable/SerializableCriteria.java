package fm.doe.national.data.data_source.models.serializable;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.Collection;

import fm.doe.national.data.data_source.models.Criteria;
import fm.doe.national.data.data_source.models.Standard;
import fm.doe.national.data.data_source.models.SubCriteria;

public class SerializableCriteria implements Criteria {

    @SerializedName("subcriteria")
    private Collection<SerializableSubCriteria> subCriterias;

    private String name;

    @Nullable
    @Override
    public Standard getStandard() {
        return null;
    }

    @NonNull
    @Override
    public String getName() {
        return name;
    }

    @Override
    public Collection<? extends SubCriteria> getSubCriterias() {
        return subCriterias;
    }
}
