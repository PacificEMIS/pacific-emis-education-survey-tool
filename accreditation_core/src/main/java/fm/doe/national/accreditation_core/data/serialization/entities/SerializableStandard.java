package fm.doe.national.accreditation_core.data.serialization.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import fm.doe.national.accreditation_core.data.model.Criteria;
import fm.doe.national.accreditation_core.data.model.Standard;
import fm.doe.national.core.data.model.Progress;

@Root(name = "standard")
public class SerializableStandard implements Standard {

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

    public SerializableStandard(@NonNull Standard other) {
        this.name = other.getTitle();
        this.index = other.getSuffix();
        if (other.getCriterias() != null) {
            this.criterias = other.getCriterias().stream().map(SerializableCriteria::new).collect(Collectors.toList());
        }
    }

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

    @NonNull
    @Override
    public List<? extends Criteria> getCriterias() {
        return criterias == null ? Collections.emptyList() : criterias;
    }

    @Override
    public long getId() {
        return 0;
    }

    @NonNull
    @Override
    public Progress getProgress() {
        throw new IllegalStateException();
    }

}
