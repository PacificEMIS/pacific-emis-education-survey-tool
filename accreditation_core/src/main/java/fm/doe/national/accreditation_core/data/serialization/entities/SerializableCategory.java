package fm.doe.national.accreditation_core.data.serialization.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.convert.Convert;

import java.util.List;
import java.util.stream.Collectors;

import fm.doe.national.accreditation_core.data.model.Category;
import fm.doe.national.accreditation_core.data.model.EvaluationForm;
import fm.doe.national.accreditation_core.data.model.ObservationInfo;
import fm.doe.national.accreditation_core.data.model.Standard;
import fm.doe.national.accreditation_core.data.serialization.EvaluationFormConverter;
import fm.doe.national.core.data.model.Progress;

@Root(name = "category")
public class SerializableCategory implements Category {

    @ElementList(inline = true)
    List<SerializableStandard> standards;

    @Element(name = "type")
    @Convert(EvaluationFormConverter.class)
    EvaluationForm evaluationForm;

    @Element
    String name;

    @Nullable
    @Element(name = "observationInfo", required = false)
    SerializableObservationInfo observationInfo;

    public SerializableCategory(@NonNull Category other) {
        this.name = other.getTitle();
        this.evaluationForm = other.getEvaluationForm();
        if (other.getStandards() != null) {
            this.standards = other.getStandards().stream().map(SerializableStandard::new).collect(Collectors.toList());
        }
        if (other.getObservationInfo() != null) {
            this.observationInfo = new SerializableObservationInfo(other.getObservationInfo());
        }
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

    @Nullable
    @Override
    public ObservationInfo getObservationInfo() {
        return observationInfo;
    }

    @NonNull
    @Override
    public Progress getProgress() {
        throw new IllegalStateException();
    }

    @Override
    public EvaluationForm getEvaluationForm() {
        return evaluationForm;
    }
}
