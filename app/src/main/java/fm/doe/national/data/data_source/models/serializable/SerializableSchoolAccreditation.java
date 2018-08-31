package fm.doe.national.data.data_source.models.serializable;

import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.List;

import fm.doe.national.data.data_source.models.CategoryProgress;
import fm.doe.national.data.data_source.models.GroupStandard;

@Xml(name = "schoolAccreditation")
public class SerializableSchoolAccreditation implements LinkedSchoolAccreditation, ListConverter.Converter<GroupStandard, SerializableGroupStandard> {

    @PropertyElement
    String type;

    @PropertyElement
    int version;

    @Element
    List<SerializableGroupStandard> groupStandards;

    public SerializableSchoolAccreditation() {
    }

    public SerializableSchoolAccreditation(LinkedSchoolAccreditation schoolAccreditation) {
        this.type = schoolAccreditation.getType();
        this.version = schoolAccreditation.getVersion();
        this.groupStandards = ListConverter.createList(schoolAccreditation.getGroupStandards(), this);
    }

    @Override
    public CategoryProgress getCategoryProgress() {
        return null;
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

    @Override
    public List<? extends LinkedGroupStandard> getGroupStandards() {
        return groupStandards;
    }
}
