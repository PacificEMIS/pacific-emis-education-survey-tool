package fm.doe.national.data.data_source.models.serializable;

import android.support.annotation.NonNull;

import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.List;

import fm.doe.national.data.data_source.models.CategoryProgress;
import fm.doe.national.data.data_source.models.GroupStandard;

@Xml(name = "groupStandard")
public class SerializableGroupStandard implements LinkedGroupStandard, ListConverter.Converter<LinkedStandard, SerializableStandard>  {

    @Element
    List<SerializableStandard> standards;

    @PropertyElement
    String name;

    public SerializableGroupStandard() {
    }

    public SerializableGroupStandard(GroupStandard input) {
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
    public Long getId() {
        return null;
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
