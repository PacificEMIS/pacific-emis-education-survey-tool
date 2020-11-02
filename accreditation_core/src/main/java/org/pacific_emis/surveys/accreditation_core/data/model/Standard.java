package org.pacific_emis.surveys.accreditation_core.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import org.pacific_emis.surveys.core.data.model.IdentifiedObject;
import org.pacific_emis.surveys.core.data.model.Progressable;

public interface Standard extends Progressable, IdentifiedObject {

    @NonNull
    String getTitle();

    @NonNull
    String getSuffix();

    @Nullable
    List<? extends Criteria> getCriterias();

}
