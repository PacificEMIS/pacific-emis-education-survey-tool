package fm.doe.national.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
