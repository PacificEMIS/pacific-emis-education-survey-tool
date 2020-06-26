package org.pacific_emis.surveys.survey_core.ui.photos;

import androidx.annotation.NonNull;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import java.io.File;
import java.util.List;

import org.pacific_emis.surveys.core.data.model.Photo;
import org.pacific_emis.surveys.core.ui.screens.base.BaseView;

public interface PhotosView extends BaseView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showPhotos(List<Photo> photos);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void takePictureTo(@NonNull File file);

}
