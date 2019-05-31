package fm.doe.national.cloud.ui.cloud.dropbox;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import fm.doe.national.cloud.model.dropbox.BrowsingTreeObject;
import fm.doe.national.core.ui.screens.base.BaseView;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface DropboxView extends BaseView {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void startAuthentication();

    void exit();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showPicker(PickerType pickerType, BrowsingTreeObject root);

    enum Action {
        AUTH, PICK
    }

    enum PickerType {
        FILE, FOLDER
    }

}
