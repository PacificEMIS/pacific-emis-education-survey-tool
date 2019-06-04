package fm.doe.national.accreditation_core.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import fm.doe.national.core.data.model.IdentifiedObject;

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
