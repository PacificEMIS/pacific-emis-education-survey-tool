package fm.doe.national.offline_sync.ui.devices;

import android.content.Context;
import android.content.Intent;

import com.omegar.mvp.InjectViewState;

import fm.doe.national.offline_sync.data.model.Device;
import fm.doe.national.offline_sync.di.OfflineSyncComponent;
import fm.doe.national.offline_sync.domain.InteractiveOfflineSyncUseCase;
import fm.doe.national.offline_sync.ui.base.BaseBluetoothPresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class PairedDevicesPresenter extends BaseBluetoothPresenter<PairedDevicesView> {

    private final InteractiveOfflineSyncUseCase executingUseCase;

    public PairedDevicesPresenter(OfflineSyncComponent component) {
        super(component.getAccessor());
        executingUseCase = (InteractiveOfflineSyncUseCase) component.getUseCase();
        addDisposable(
                offlineAccessor.getDevicesSubject()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(devices -> {
                            getViewState().hideWaiting();
                            getViewState().setDevicesList(devices);
                        }, this::handleError)
        );

        loadDevices();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        offlineAccessor.stopDiscoverDevices();
    }

    private void loadDevices() {
        getViewState().showWaiting();
        offlineAccessor.discoverDevices();
    }

    public void onDevicePressed(Device device) {
        executingUseCase.onDeviceSelected(device);
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
