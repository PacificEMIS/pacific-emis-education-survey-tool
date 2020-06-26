package org.pacific_emis.surveys.accreditation_core.data.serialization.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;
import java.util.stream.Collectors;

import org.pacific_emis.surveys.accreditation_core.data.model.Criteria;
import org.pacific_emis.surveys.core.data.model.Progress;
import org.pacific_emis.surveys.accreditation_core.data.model.SubCriteria;

@Root(name = "criteria")
public class SerializableCriteria implements Criteria {

    @Element
    String name;

    @ElementList(inline = true)
    List<SerializableSubCriteria> subCriterias;

    @Nullable
    @Element(name = "id", required = false)
    String index;

    public SerializableCriteria() {
    }

    public SerializableCriteria(@NonNull Criteria other) {
        this.name = other.getTitle();
        this.index = other.getSuffix();
        if (other.getSubCriterias() != null) {
            this.subCriterias = other.getSubCriterias().stream().map(SerializableSubCriteria::new).collect(Collectors.toList());
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

    @Nullable
    @Override
    public List<? extends SubCriteria> getSubCriterias() {
        return subCriterias;
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
