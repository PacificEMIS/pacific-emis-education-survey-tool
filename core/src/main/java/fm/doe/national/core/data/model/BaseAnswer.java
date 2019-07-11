package fm.doe.national.core.data.model;

import androidx.annotation.Nullable;

import java.util.List;

public interface BaseAnswer {

    @Nullable
    List<? extends Photo> getPhotos();

    @Nullable
    String getComment();

}
