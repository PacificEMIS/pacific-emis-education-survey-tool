package org.pacific_emis.surveys.remote_storage.ui.remote_storage;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import org.pacific_emis.surveys.core.ui.screens.base.BaseView;
import org.pacific_emis.surveys.remote_storage.data.model.GoogleDriveFileHolder;

public interface DriveStorageView extends BaseView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setItems(List<GoogleDriveFileHolder> items);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setContent(String content);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setParentName(String currentParentName);

}
