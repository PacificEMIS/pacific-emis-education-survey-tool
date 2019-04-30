package fm.doe.national.data.serialization.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

import fm.doe.national.data.model.Criteria;
import fm.doe.national.data.model.SubCriteria;

@Root(name = "criteria")
public class SerializableCriteria implements Criteria, Serializable {

    @Element
    String name;

    @ElementList(inline = true)
    List<SerializableSubCriteria> subCriterias;

    @Nullable
    @Element(name = "id", required = false)
    String index;

    @NonNull
    @Override
    public String getTitle() {
        return name;
    }

    @NonNull
    @Override
    public String getSuffix() {
        return index == null ? "" : index;
    }

    @Nullable
    @Override
    public List<? extends SubCriteria> getSubCriterias() {
        return subCriterias;
    }

    @Override
    public long getId() {
        return 0;
    }
}
