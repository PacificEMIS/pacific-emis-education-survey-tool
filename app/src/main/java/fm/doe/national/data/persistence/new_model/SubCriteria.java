package fm.doe.national.data.persistence.new_model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface SubCriteria {

    @NonNull
    String getSuffix();

    @NonNull
    String getTitle();

    @Nullable
    String getInterviewQuestions();

    @Nullable
    String getHint();

    @Nullable
    String getComment();

    void setComment(@Nullable String comment);

    @Nullable
    Answer getAnswer();

}
