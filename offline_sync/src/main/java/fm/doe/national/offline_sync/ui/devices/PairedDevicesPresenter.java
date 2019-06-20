package fm.doe.national.offline_sync.ui.devices;

import android.content.Context;
import android.content.Intent;

import com.omegar.mvp.InjectViewState;

import fm.doe.national.core.ui.screens.base.BasePresenter;
import fm.doe.national.offline_sync.data.accessor.OfflineAccessor;
import fm.doe.national.offline_sync.data.model.Device;
import fm.doe.national.offline_sync.di.OfflineSyncComponent;
import fm.doe.national.offline_sync.domain.InteractiveOfflineSyncUseCase;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class PairedDevicesPresenter extends BasePresenter<PairedDevicesView> {

    private final OfflineAccessor accessor;
    private final InteractiveOfflineSyncUseCase executingUseCase;

    public PairedDevicesPresenter(OfflineSyncComponent component) {
        accessor = component.getAccessor();
        executingUseCase = (InteractiveOfflineSyncUseCase) component.getUseCase();
        addDisposable(
                accessor.getDevicesSubject()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(devices -> {
                            getViewState().hideWaiting();
                            getViewState().setDevicesList(devices);
                        }, this::handleError)
        );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accessor.stopDiscoverDevices();
    }

    @Override
    public void attachView(PairedDevicesView view) {
        super.attachView(view);
        getViewState().askBluetoothPermissions();
    }

    public void onBluetoothPermissionsGranted() {
        loadDevices();
    }

    private void loadDevices() {
        getViewState().showWaiting();
        accessor.discoverDevices();
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
        accessor.onBroadcastReceive(context, intent);
    }
}
