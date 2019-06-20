package fm.doe.national.offline_sync.data.accessor;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import fm.doe.national.core.data.exceptions.NotImplementedException;
import fm.doe.national.core.data.model.Survey;
import fm.doe.national.offline_sync.data.model.BluetoothDeviceWrapper;
import fm.doe.national.offline_sync.data.model.Device;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public final class BluetoothOfflineAccessor implements OfflineAccessor {

    public static final List<String> sReceiverActionsToRegister = Arrays.asList(
            BluetoothDevice.ACTION_FOUND,
            BluetoothAdapter.ACTION_DISCOVERY_STARTED,
            BluetoothAdapter.ACTION_DISCOVERY_FINISHED
    );

    private final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private final Subject<List<Device>> devicesSubject = PublishSubject.create();
    private final List<BluetoothDevice> devicesCache = new ArrayList<>();

    @Override
    public Subject<List<Device>> getDevicesSubject() {
        return devicesSubject;
    }

    @Override
    public void discoverDevices() {
        startDiscoverDevices();
    }

    private void startDiscoverDevices() {
        if (!bluetoothAdapter.isEnabled()) {
            return;
        }

        bluetoothAdapter.startDiscovery();
    }

    @Override
    public void stopDiscoverDevices() {
        bluetoothAdapter.cancelDiscovery();
    }

    @Override
    public Completable connect(Device device) {
        return Completable.complete();
    }

    @Override
    public Completable disconnect(Device device) {
        return Completable.complete();
    }

    @Override
    public Single<List<Survey>> requestSurveys(Device device) {
        return Single.just(Collections.emptyList());
    }

    @Override
    public Single<Survey> requestFilledSurvey(Device device, long surveyId) {
        return Single.error(new NotImplementedException());
    }

    @Override
    public void onBroadcastReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
            devicesCache.clear();
        } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
            devicesSubject.onNext(devicesCache.stream().map(BluetoothDeviceWrapper::new).collect(Collectors.toList()));
        } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            devicesCache.add(intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE));
        }
    }
}
