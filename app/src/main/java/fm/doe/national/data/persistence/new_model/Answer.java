package fm.doe.national.data.persistence.new_model;

import java.util.List;

import androidx.annotation.Nullable;

public interface Answer extends IdentifiedObject {

    AnswerState getState();

    void setState(AnswerState state);

    @Nullable
    String getComment();

    void setComment(@Nullable String comment);

    List<Photo> getPhotos();

    void setPhotos(List<Photo> photos);
}
