package fm.doe.national.data.data_source.models.serializable;

import java.util.Collection;

import fm.doe.national.data.data_source.models.GroupStandard;
import fm.doe.national.data.data_source.models.SchoolAccreditation;

public class SerializableSchoolAccreditation implements SchoolAccreditation {

    private String type;

    private int version;

    private Collection<SerializableGroupStandard> groupStandards;

    @Override
    public Collection<? extends GroupStandard> getGroupStandards() {
        return groupStandards;
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public String getType() {
        return type;
    }
}
