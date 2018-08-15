package fm.doe.national.data.data_source.models.survey.serializable;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Collection;

import fm.doe.national.data.data_source.models.survey.Criteria;
import fm.doe.national.data.data_source.models.survey.GroupStandard;
import fm.doe.national.data.data_source.models.survey.Standard;
import fm.doe.national.data.data_source.models.survey.db.OrmLiteCriteria;

public class SerializableStandard implements Standard {

    private String name;

    @SerializedName("criteria")
    private Collection<OrmLiteCriteria> criterias;

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
