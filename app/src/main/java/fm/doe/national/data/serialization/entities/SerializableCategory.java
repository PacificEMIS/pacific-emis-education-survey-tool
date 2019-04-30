package fm.doe.national.data.serialization.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

import fm.doe.national.data.model.Category;
import fm.doe.national.data.model.Standard;

@Root(name = "category")
public class SerializableCategory implements Category, Serializable {

    @ElementList(inline = true)
    List<SerializableStandard> standards;

    @Element
    String name;

    @NonNull
    @Override
    public String getTitle() {
        return name;
    }

    @Nullable
    @Override
    public List<? extends Standard> getStandards() {
        return standards;
    }

    @Override
    public long getId() {
        return 0;
    }
}
