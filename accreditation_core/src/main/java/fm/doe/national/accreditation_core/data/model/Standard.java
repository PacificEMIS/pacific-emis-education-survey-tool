package fm.doe.national.accreditation_core.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import fm.doe.national.core.data.model.IdentifiedObject;
import fm.doe.national.core.data.model.Progressable;

public interface Standard extends Progressable, IdentifiedObject {

    @NonNull
    String getTitle();

    @NonNull
    String getSuffix();

    @Nullable
    List<? extends Criteria> getCriterias();

    @Nullable
    ObservationInfo getObservationInfo();
}
