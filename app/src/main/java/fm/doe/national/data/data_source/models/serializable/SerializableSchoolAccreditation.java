package fm.doe.national.data.data_source.models.serializable;

import com.google.gson.annotations.SerializedName;
import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;


import java.util.Collection;
import java.util.List;

import fm.doe.national.data.data_source.models.GroupStandard;
import fm.doe.national.data.data_source.models.SchoolAccreditation;

@Xml
public class SerializableSchoolAccreditation implements SchoolAccreditation {

    @PropertyElement
    private String type;

    @PropertyElement
    private int version;

    @Element
    List<SerializableGroupStandard> groupStandards;

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

    public void setType(String type) {
        this.type = type;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setGroupStandards(List<SerializableGroupStandard> groupStandards) {
        this.groupStandards = groupStandards;
    }
}
