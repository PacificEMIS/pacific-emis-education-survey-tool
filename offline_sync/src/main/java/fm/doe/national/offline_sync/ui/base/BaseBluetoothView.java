package fm.doe.national.offline_sync.ui.base;

import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import fm.doe.national.core.ui.screens.base.BaseView;

public interface BaseBluetoothView extends BaseView {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void requestBluetoothPermissions();


    @StateStrategyType(OneExecutionStateStrategy.class)
    void requestBluetoothDiscoverability();

}
