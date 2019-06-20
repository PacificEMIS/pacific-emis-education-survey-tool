package fm.doe.national.offline_sync.ui.devices;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import fm.doe.national.core.ui.screens.base.BaseView;
import fm.doe.national.offline_sync.data.model.Device;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface PairedDevicesView extends BaseView {

    void setDevicesList(List<Device> devices);

    void askBluetoothPermissions();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToDeviceSettings();

}
