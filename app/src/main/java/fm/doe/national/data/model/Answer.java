package fm.doe.national.data.model;

import androidx.annotation.Nullable;

public interface Answer extends IdentifiedObject {

    AnswerState getState();

    void setState(AnswerState state);

    @Nullable
    String getComment();

    void setComment(@Nullable String comment);
}
