package fm.doe.national.ui.screens.photos;

import java.util.List;

import fm.doe.national.ui.screens.base.BaseView;

public interface PhotosView extends BaseView {
    void showPhotos(List<String> photos);
}
