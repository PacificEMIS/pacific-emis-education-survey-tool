package fm.doe.national.remote_storage.ui.remote_storage;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import fm.doe.national.core.ui.screens.base.BaseView;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface DriveStorageView extends BaseView {

}
