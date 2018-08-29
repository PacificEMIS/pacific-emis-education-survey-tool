package fm.doe.national.ui.screens.cloud;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import fm.doe.national.data.cloud.dropbox.BrowsingTreeObject;
import fm.doe.national.ui.screens.base.BaseView;

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
