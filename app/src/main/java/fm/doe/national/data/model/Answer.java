package fm.doe.national.data.model;

import androidx.annotation.Nullable;

import java.util.List;

public interface Answer extends IdentifiedObject {

    AnswerState getState();

    @Nullable
    String getComment();

    @Nullable
    List<? extends Photo> getPhotos();

}
