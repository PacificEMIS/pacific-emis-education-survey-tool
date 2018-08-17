package fm.doe.national.data.data_source.models.serializable;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import fm.doe.national.data.data_source.models.Criteria;
import fm.doe.national.data.data_source.models.SubCriteria;

public class SerializableSubCriteria implements SubCriteria {

    private String name;

    @Nullable
    @Override
    public Criteria getCriteria() {
        return null;
    }

    @NonNull
    @Override
    public String getName() {
        return name;
    }
}
