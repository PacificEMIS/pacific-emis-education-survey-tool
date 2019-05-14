package fm.doe.national.data.serialization.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

import fm.doe.national.data.model.Category;
import fm.doe.national.data.model.Standard;
import fm.doe.national.app_support.utils.CollectionUtils;

@Root(name = "category")
public class SerializableCategory implements Category {

    @ElementList(inline = true)
    List<SerializableStandard> standards;

    @Element
    String name;

    public SerializableCategory(@NonNull Category other) {
        this.name = other.getTitle();
        this.standards = CollectionUtils.map(other.getStandards(), SerializableStandard::new);
    }

    public SerializableCategory() {
    }

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
