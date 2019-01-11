package fm.doe.national.data.data_source.models.serializable;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

import androidx.annotation.NonNull;
import fm.doe.national.data.data_source.models.CategoryProgress;

@Root(name = "category")
public class SerializableCategory implements LinkedCategory, ListConverter.Converter<LinkedStandard, SerializableStandard>  {

    @ElementList(inline = true)
    List<SerializableStandard> standards;

    @Element
    String name;

    public SerializableCategory() {
    }

    public SerializableCategory(LinkedCategory input) {
        name = input.getName();
        standards = ListConverter.createList(input.getStandards(), this);
    }

    @NonNull
    @Override
    public String getName() {
        return name;
    }

    @Override
    public CategoryProgress getCategoryProgress() {
        return null;
    }

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public SerializableStandard convert(LinkedStandard input) {
        return new SerializableStandard(input);
    }

    @Override
    public List<? extends LinkedStandard> getStandards() {
        return standards;
    }
}
