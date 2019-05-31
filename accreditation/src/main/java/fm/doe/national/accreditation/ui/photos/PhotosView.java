package fm.doe.national.accreditation.ui.photos;

import androidx.annotation.NonNull;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import java.io.File;
import java.util.List;

import fm.doe.national.core.data.model.Photo;
import fm.doe.national.core.ui.screens.base.BaseView;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface PhotosView extends BaseView {
    void showPhotos(List<Photo> photos);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void takePictureTo(@NonNull File file);
}
