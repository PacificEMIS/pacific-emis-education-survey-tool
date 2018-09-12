package fm.doe.national.data.data_source.models.serializable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

import fm.doe.national.data.data_source.models.CategoryProgress;

@Root(name = "schoolAccreditation")
public class SerializableSchoolAccreditation implements LinkedSchoolAccreditation, ListConverter.Converter<LinkedCategory, SerializableCategory> {

    @Element
    String type;

    @Element
    int version;

    @ElementList(inline = true)
    List<SerializableCategory> categories;

    public SerializableSchoolAccreditation() {
    }

    public SerializableSchoolAccreditation(LinkedSchoolAccreditation schoolAccreditation) {
        this.type = schoolAccreditation.getType();
        this.version = schoolAccreditation.getVersion();
        this.categories = ListConverter.createList(schoolAccreditation.getCategories(), this);
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
    public SerializableCategory convert(LinkedCategory input) {
        return new SerializableCategory(input);
    }

    @Override
    public List<? extends LinkedCategory> getCategories() {
        return categories;
    }
}
