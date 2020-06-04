package fm.doe.national.remote_storage.ui.remote_storage;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import fm.doe.national.core.ui.screens.base.BaseView;
import fm.doe.national.remote_storage.data.model.GoogleDriveFileHolder;

public interface DriveStorageView extends BaseView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setItems(List<GoogleDriveFileHolder> items);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setContent(String content);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void close();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setParentName(String currentParentName);

}
