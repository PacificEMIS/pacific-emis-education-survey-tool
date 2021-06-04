package org.pacific_emis.surveys.accreditation_core.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.pacific_emis.surveys.core.data.model.IdentifiedObject;

public interface SubCriteria extends IdentifiedObject {

    @NonNull
    String getSuffix();

    @NonNull
    String getTitle();

    @Nullable
    String getInterviewQuestions();

    @Nullable
    String getHint();

    @Nullable
    Answer getAnswer();

}
