package fm.doe.national.data.data_source.models.serializable;

import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.List;

import fm.doe.national.data.data_source.models.GroupStandard;
import fm.doe.national.data.data_source.models.SchoolAccreditation;

@Xml(name = "schoolAccreditation")
public class SerializableSchoolAccreditation implements SchoolAccreditation {

    @PropertyElement
    String type;

    @PropertyElement
    int version;

    @Element
    List<SerializableGroupStandard> groupStandards;

    @Override
    public List<? extends GroupStandard> getGroupStandards() {
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
