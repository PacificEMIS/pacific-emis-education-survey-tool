package fm.doe.national.offline_sync.ui.devices;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import fm.doe.national.offline_sync.data.model.Device;
import fm.doe.national.offline_sync.ui.base.BaseBluetoothView;

public interface PairedDevicesView extends BaseBluetoothView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setDevicesList(List<Device> devices);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setListLoadingVisible(boolean visible);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToDeviceSettings();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToSurveys();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setNextEnabled(boolean enabled);

}
