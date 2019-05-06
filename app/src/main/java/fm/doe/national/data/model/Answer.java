package fm.doe.national.data.model;

import androidx.annotation.Nullable;

public interface Answer extends IdentifiedObject {

    AnswerState getState();

    @Nullable
    String getComment();

}
