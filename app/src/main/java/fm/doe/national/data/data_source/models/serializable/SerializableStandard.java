package fm.doe.national.data.data_source.models.serializable;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import fm.doe.national.data.data_source.models.CategoryProgress;
import fm.doe.national.data.data_source.models.Criteria;

@Root(name = "standard")
public class SerializableStandard implements LinkedStandard, ListConverter.Converter<Criteria, SerializableCriteria> {

    @Element
    String name;

    @Nullable
    @ElementList(inline = true, required = false)
    List<SerializableCriteria> criterias;

    @Nullable
    @Element(name = "id", required = false)
    String index;

    public SerializableStandard() {
    }

    public SerializableStandard(LinkedStandard standard) {
        name = standard.getName();
        index = standard.getIndex();
        criterias = ListConverter.createList(standard.getCriterias(), this);
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
    public SerializableCriteria convert(Criteria input) {
        return new SerializableCriteria(input);
    }

    @Nullable
    @Override
    public List<? extends Criteria> getCriterias() {
        return criterias;
    }

    @Nullable
    @Override
    public String getIndex() {
        return index;
    }
}
