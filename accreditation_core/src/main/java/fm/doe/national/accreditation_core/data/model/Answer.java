package fm.doe.national.accreditation_core.data.model;

import androidx.annotation.Nullable;

import java.util.List;

import fm.doe.national.core.data.model.IdentifiedObject;
import fm.doe.national.core.data.model.Photo;

public interface Answer extends IdentifiedObject {

    AnswerState getState();

    @Nullable
    String getComment();

    @Nullable
    List<? extends Photo> getPhotos();

}
