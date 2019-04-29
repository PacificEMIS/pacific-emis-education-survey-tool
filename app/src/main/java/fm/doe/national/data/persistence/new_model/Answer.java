package fm.doe.national.data.persistence.new_model;

import java.util.List;

import androidx.annotation.Nullable;

public interface Answer {

    State getState();

    void setState(State state);

    @Nullable
    String getComment();

    void setComment(@Nullable String comment);

    List<PhotoEntity> getPhotos();

    void setPhotos(List<PhotoEntity> photos);

    enum State {
        NOT_ANSWERED, POSITIVE, NEGATIVE
    }
}
