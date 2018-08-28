package fm.doe.national.data.data_source.models.serializable;

import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.ArrayList;
import java.util.List;

import fm.doe.national.data.data_source.models.GroupStandard;
import fm.doe.national.data.data_source.models.SchoolAccreditation;
import io.reactivex.Observable;

@Xml(name = "schoolAccreditation")
public class SerializableSchoolAccreditation implements SchoolAccreditation, ListConverter.Converter<GroupStandard, SerializableGroupStandard> {

    @PropertyElement
    String type;

    @PropertyElement
    int version;

    @Element
    List<SerializableGroupStandard> groupStandards;

    public SerializableSchoolAccreditation() {
    }

    public SerializableSchoolAccreditation(SchoolAccreditation schoolAccreditation) {
        this.type = schoolAccreditation.getType();
        this.version = schoolAccreditation.getVersion();
        this.groupStandards = ListConverter.createList(schoolAccreditation.getGroupStandards(), this);
    }

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

    @Override
    public SerializableGroupStandard convert(GroupStandard input) {
        return new SerializableGroupStandard(input);
    }
}
