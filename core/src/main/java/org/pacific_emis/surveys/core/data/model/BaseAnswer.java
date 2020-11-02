package org.pacific_emis.surveys.core.data.model;

import androidx.annotation.Nullable;

import java.util.List;

public interface BaseAnswer {

    @Nullable
    List<? extends Photo> getPhotos();

    @Nullable
    String getComment();

}
