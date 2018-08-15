package fm.doe.national.data.data_source.models.survey.serializable;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Collection;

import fm.doe.national.data.data_source.models.survey.Criteria;
import fm.doe.national.data.data_source.models.survey.Standard;
import fm.doe.national.data.data_source.models.survey.SubCriteria;
import fm.doe.national.data.data_source.models.survey.db.OrmLiteStandard;
import fm.doe.national.data.data_source.models.survey.db.OrmLiteSubCriteria;

public class SerializableCriteria implements Criteria {

    @SerializedName("subcriteria")
    private Collection<SubCriteria> subCriterias;

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
