package fm.doe.national.data.converters;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Collection;

import fm.doe.national.data.models.survey.GroupStandard;

public class GroupStandardWrapper implements Serializable {

    @SerializedName("group_standard")
    protected Collection<GroupStandard> groupStandards;

    public Collection<GroupStandard> getGroupStandards() {
        return groupStandards;
    }

    public void setGroupStandards(Collection<GroupStandard> groupStandards) {
        this.groupStandards = groupStandards;
    }
}
