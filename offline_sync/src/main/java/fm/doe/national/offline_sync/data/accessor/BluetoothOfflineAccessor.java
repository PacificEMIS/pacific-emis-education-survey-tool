package fm.doe.national.offline_sync.data.accessor;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import fm.doe.national.core.data.exceptions.NotImplementedException;
import fm.doe.national.core.data.model.Survey;
import fm.doe.national.offline_sync.data.bluetooth_threads.Acceptor;
import fm.doe.national.offline_sync.data.bluetooth_threads.ConnectionState;
import fm.doe.national.offline_sync.data.bluetooth_threads.Connector;
import fm.doe.national.offline_sync.data.bluetooth_threads.Transporter;
import fm.doe.national.offline_sync.data.model.BluetoothDeviceWrapper;
import fm.doe.national.offline_sync.data.model.Device;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.functions.Action;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.CompletableSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public final class BluetoothOfflineAccessor implements OfflineAccessor, Connector.OnConnectionAttemptListener, Transporter.Listener, Acceptor.OnSocketAcceptedListener {

    public static final List<String> sReceiverActionsToRegister = Arrays.asList(
            BluetoothDevice.ACTION_FOUND,
            BluetoothAdapter.ACTION_DISCOVERY_STARTED,
            BluetoothAdapter.ACTION_DISCOVERY_FINISHED
    );

    private final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private final WeakReference<Context> applicationContextRef;
    private final Subject<List<Device>> devicesSubject = PublishSubject.create();
    private final Subject<ConnectionState> connectionStateSubject = BehaviorSubject.createDefault(ConnectionState.NONE);
    private final Subject<Action> discoverableRequestSubject = PublishSubject.create();
    private final Subject<Action> btPermissionsRequestSubject = PublishSubject.create();
    private final List<BluetoothDevice> devicesCache = new ArrayList<>();

    private ConnectionState connectionState = ConnectionState.NONE;

    @Nullable
    private Acceptor acceptor;

    @Nullable
    private Connector connector;

    @Nullable
    private Transporter transporter;

    @Nullable
    private CompletableSubject connectionSubject;

    public BluetoothOfflineAccessor(Context applicationContext) {
        this.applicationContextRef = new WeakReference<>(applicationContext);
    }

    @Override
    public Subject<List<Device>> getDevicesSubject() {
        return devicesSubject;
    }

    @Override
    public Subject<ConnectionState> getConnectionStateSubject() {
        return connectionStateSubject;
    }

    @Override
    public Subject<Action> getDiscoverableRequestSubject() {
        return discoverableRequestSubject;
    }

    @Override
    public Subject<Action> getPermissionsRequestSubject() {
        return btPermissionsRequestSubject;
    }

    @Override
    public void discoverDevices() {
        startDiscoverDevices();
    }

    private void startDiscoverDevices() {
        if (!bluetoothAdapter.isEnabled()) {
            return;
        }

        doWithBluetoothPermissions(bluetoothAdapter::startDiscovery);
    }

    @Override
    public void stopDiscoverDevices() {
        bluetoothAdapter.cancelDiscovery();
    }

    @Override
    public void becomeAvailableToConnect() {
        killAll();

        Context appContext = applicationContextRef.get();

        if (appContext == null) {
            return;
        }

        doWithBluetoothPermissions(() -> doInDiscoverableMode(() -> {
            acceptor = new Acceptor(appContext, bluetoothAdapter, this);
            setConnectionState(ConnectionState.LISTENING);
        }));
    }

    private void doWithBluetoothPermissions(Action action) {
        btPermissionsRequestSubject.onNext(action);
    }

    private void doInDiscoverableMode(Action action) {
        discoverableRequestSubject.onNext(action);
    }

    @Override
    public void becomeUnavailableToConnect() {
        onDisconnect();
    }

    @Override
    public Completable connect(Device device) {
        BluetoothDevice btDevice = ((BluetoothDeviceWrapper) device).getBtDevice();

        killAll();

        Context appContext = applicationContextRef.get();

        if (appContext == null) {
            return CompletableSubject.error(new IllegalStateException());
        }

        setConnectionState(ConnectionState.CONNECTING);
        connectionSubject = CompletableSubject.create();
        connector = new Connector(appContext, bluetoothAdapter, btDevice, this);
        connector.start();
        return connectionSubject;
    }

    @Override
    public void disconnect() {
        onDisconnect();
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

    @Override
    public void onConnectionAttempted(BluetoothSocket socket) {
        if (connectionSubject != null) {
            connectionSubject.onComplete();
        }

        establishConnection(socket);
    }

    @Override
    public void onSocketAccept(BluetoothSocket socket) {
        establishConnection(socket);
    }

    private void establishConnection(BluetoothSocket socket) {
        killAll();
        transporter = new Transporter(socket, this);
        setConnectionState(ConnectionState.CONNECTED);
        transporter.setState(connectionState);
        transporter.start();
    }

    @Override
    public void onMessageObtain(byte[] buffer, int byteCount) {

    }

    @Override
    public void onConnectionLost() {
        onDisconnect();
    }

    private void onDisconnect() {
        setConnectionState(ConnectionState.NONE);
        killAll();
    }

    private void killAll() {
        killTransporter();
        killAcceptor();
        killConnector();
    }

    private void killTransporter() {
        if (transporter != null) {
            transporter.end();
            transporter = null;
        }
    }

    private void killAcceptor() {
        if (acceptor != null) {
            acceptor.end();
            acceptor = null;
        }
    }

    private void killConnector() {
        if (connector != null) {
            connector.end();
            connector = null;
        }
    }

    private void setConnectionState(ConnectionState connectionState) {
        this.connectionState = connectionState;
        connectionStateSubject.onNext(this.connectionState);
    }

}