package fm.doe.national.ui.screens.photos;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import fm.doe.national.ui.screens.base.BaseView;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface PhotosView extends BaseView {
    void showPhotos(List<String> photos);
}
