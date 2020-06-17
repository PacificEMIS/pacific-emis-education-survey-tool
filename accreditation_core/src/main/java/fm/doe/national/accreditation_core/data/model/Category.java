package fm.doe.national.accreditation_core.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import fm.doe.national.core.data.model.IdentifiedObject;
import fm.doe.national.core.data.model.Progressable;

public interface Category extends Progressable, IdentifiedObject {

    @NonNull
    String getTitle();

    @Nullable
    List<? extends Standard> getStandards();

    EvaluationForm getEvaluationForm();

    @Nullable
    ObservationInfo getObservationInfo();
}
