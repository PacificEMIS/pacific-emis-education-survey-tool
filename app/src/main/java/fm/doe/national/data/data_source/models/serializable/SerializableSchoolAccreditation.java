package fm.doe.national.data.data_source.models.serializable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

import fm.doe.national.data.data_source.models.CategoryProgress;

@Root(name = "schoolAccreditation")
public class SerializableSchoolAccreditation implements LinkedSchoolAccreditation, ListConverter.Converter<LinkedGroupStandard, SerializableGroupStandard> {

    @Element
    String type;

    @Element
    int version;

    @ElementList(inline = true)
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
    public SerializableGroupStandard convert(LinkedGroupStandard input) {
        return new SerializableGroupStandard(input);
    }

    @Override
    public List<? extends LinkedGroupStandard> getGroupStandards() {
        return groupStandards;
    }
}
