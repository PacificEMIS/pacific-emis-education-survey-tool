package fm.doe.national.offline_sync.ui.devices;

import android.content.Context;
import android.content.Intent;

import com.omegar.mvp.InjectViewState;

import fm.doe.national.offline_sync.data.model.Device;
import fm.doe.national.offline_sync.di.OfflineSyncComponent;
import fm.doe.national.offline_sync.ui.base.BaseBluetoothPresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class PairedDevicesPresenter extends BaseBluetoothPresenter<PairedDevicesView> {

    public PairedDevicesPresenter(OfflineSyncComponent component) {
        super(component.getAccessor());
        addDisposable(
                offlineAccessor.getDevicesObservable()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(devices -> {
                            getViewState().setListLoadingVisible(false);
                            getViewState().setDevicesList(devices);
                        }, this::handleError)
        );

        loadDevices();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        offlineAccessor.disconnect();
        offlineAccessor.stopDiscoverDevices();
    }

    private void loadDevices() {
        getViewState().setListLoadingVisible(true);
        offlineAccessor.discoverDevices();
    }

    public void onDevicePressed(Device device) {
        offlineAccessor.disconnect();
        addDisposable(
                offlineAccessor.connect(device)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(d -> getViewState().showWaiting())
                        .doFinally(getViewState()::hideWaiting)
                        .subscribe(getViewState()::navigateToSurveys, this::handleError)
        );
    }

    public void onOpenSettingsPressed() {
        getViewState().navigateToDeviceSettings();
    }

    public void onRefresh() {
        loadDevices();
    }

    public void onBroadcastReceive(Context context, Intent intent) {
        offlineAccessor.onBroadcastReceive(context, intent);
    }
}
