package fm.doe.national.offline_sync.ui.devices;

import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import fm.doe.national.offline_sync.data.model.Device;
import fm.doe.national.offline_sync.ui.base.BaseBluetoothView;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface PairedDevicesView extends BaseBluetoothView {

    void setDevicesList(List<Device> devices);

    void setListLoadingVisible(boolean visible);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToDeviceSettings();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToSurveys();

    void setNextButtonEnabled(boolean enabled);
}
