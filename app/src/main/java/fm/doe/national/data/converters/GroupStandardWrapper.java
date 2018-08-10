package fm.doe.national.data.converters;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Collection;

import fm.doe.national.data.data_source.db.models.survey.OrmLiteGroupStandard;

public class GroupStandardWrapper implements Serializable {

    @SerializedName("group_standard")
    protected Collection<OrmLiteGroupStandard> groupStandards;

    public Collection<OrmLiteGroupStandard> getGroupStandards() {
        return groupStandards;
    }

    public void setGroupStandards(Collection<OrmLiteGroupStandard> groupStandards) {
        this.groupStandards = groupStandards;
    }
}
