package fm.doe.national.remote_storage.ui.remote_storage;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import fm.doe.national.core.ui.screens.base.BaseView;
import fm.doe.national.remote_storage.data.model.GoogleDriveFileHolder;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface DriveStorageView extends BaseView {

    void setItems(List<GoogleDriveFileHolder> items);

    void setContent(String content);

}
