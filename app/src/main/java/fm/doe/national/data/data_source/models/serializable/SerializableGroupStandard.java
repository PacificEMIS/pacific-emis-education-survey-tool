package fm.doe.national.data.data_source.models.serializable;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.List;

import fm.doe.national.data.data_source.models.GroupStandard;
import fm.doe.national.data.data_source.models.Standard;

@Xml(name = "groupStandard")
public class SerializableGroupStandard implements GroupStandard, ListConverter.Converter<Standard, SerializableStandard>  {

    @Element
    List<SerializableStandard> standards;

    public SerializableGroupStandard() {
    }

    public SerializableGroupStandard(GroupStandard input) {
        this.standards = ListConverter.createList(input.getStandards(), this);
    }

    @NonNull
    @Override
    public String getName() {
        return "";
    }

    @Nullable
    public List<? extends Standard> getStandards() {
        return standards;
    }

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public SerializableStandard convert(Standard input) {
        return new SerializableStandard(input);
    }
}
