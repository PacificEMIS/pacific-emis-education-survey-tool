package fm.doe.national.data.data_source.models.serializable;

import android.support.annotation.NonNull;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

import fm.doe.national.data.data_source.models.CategoryProgress;

@Root(name = "groupStandard")
public class SerializableGroupStandard implements LinkedGroupStandard, ListConverter.Converter<LinkedStandard, SerializableStandard>  {

    @ElementList(inline = true)
    List<SerializableStandard> standards;

    @Element
    String name;

    public SerializableGroupStandard() {
    }

    public SerializableGroupStandard(LinkedGroupStandard input) {
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
