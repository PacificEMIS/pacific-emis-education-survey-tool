package org.pacific_emis.surveys.offline_sync.ui.devices;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.omegar.mvp.InjectViewState;

import org.pacific_emis.surveys.offline_sync.data.model.Device;
import org.pacific_emis.surveys.offline_sync.di.OfflineSyncComponent;
import org.pacific_emis.surveys.offline_sync.ui.base.BaseBluetoothPresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class PairedDevicesPresenter extends BaseBluetoothPresenter<PairedDevicesView> {

    @Nullable
    private Device selectedDevice;

    public PairedDevicesPresenter(OfflineSyncComponent component) {
        super(component.getAccessor());
        getViewState().setNextEnabled(false);

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
        selectedDevice = device;
        getViewState().setNextEnabled(true);
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

    public void onNextPressed() {
        if (selectedDevice == null) {
            return;
        }

        offlineAccessor.disconnect();
        addDisposable(
                offlineAccessor.connect(selectedDevice)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(d -> getViewState().showWaiting())
                        .doFinally(getViewState()::hideWaiting)
                        .subscribe(getViewState()::navigateToSurveys, this::handleError)
        );
    }
}
