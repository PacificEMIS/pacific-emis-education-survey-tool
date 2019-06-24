package fm.doe.national.offline_sync.data.accessor;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import fm.doe.national.core.data.exceptions.NotImplementedException;
import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.preferences.entities.AppRegion;
import fm.doe.national.core.preferences.entities.SurveyType;
import fm.doe.national.offline_sync.data.bluetooth_threads.Acceptor;
import fm.doe.national.offline_sync.data.bluetooth_threads.ConnectionState;
import fm.doe.national.offline_sync.data.bluetooth_threads.Connector;
import fm.doe.national.offline_sync.data.bluetooth_threads.Transporter;
import fm.doe.national.offline_sync.data.model.BluetoothDeviceWrapper;
import fm.doe.national.offline_sync.data.model.BtMessage;
import fm.doe.national.offline_sync.data.model.Device;
import fm.doe.national.offline_sync.data.model.ResponseWashSurveysBody;
import fm.doe.national.wash_core.data.model.mutable.MutableWashSurvey;
import fm.doe.national.wash_core.data.serialization.model.SerializableWashSurvey;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.functions.Action;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.CompletableSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.SingleSubject;
import io.reactivex.subjects.Subject;

public final class BluetoothOfflineAccessor implements OfflineAccessor, Transporter.Listener, Acceptor.OnSocketAcceptedListener {

    public static final List<String> sReceiverActionsToRegister = Arrays.asList(
            BluetoothDevice.ACTION_FOUND,
            BluetoothAdapter.ACTION_DISCOVERY_STARTED,
            BluetoothAdapter.ACTION_DISCOVERY_FINISHED
    );

    private final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .create();
    private final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private final WeakReference<Context> applicationContextRef;
    private final Subject<List<Device>> devicesSubject = PublishSubject.create();
    private final Subject<ConnectionState> connectionStateSubject = BehaviorSubject.createDefault(ConnectionState.NONE);
    private final Subject<Action> discoverableRequestSubject = PublishSubject.create();
    private final Subject<Action> btPermissionsRequestSubject = PublishSubject.create();
    private final List<BluetoothDevice> devicesCache = new ArrayList<>();

    @Nullable
    private SingleSubject<List<Survey>> requestSurveysSubject;

    private ConnectionState connectionState = ConnectionState.NONE;

    @Nullable
    private Acceptor acceptor;

    @Nullable
    private Connector connector;

    @Nullable
    private Transporter transporter;

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
            acceptor.start();
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
        connector = new Connector(appContext, bluetoothAdapter, btDevice);
        return connector.connect()
                .flatMapCompletable(socket -> Completable.fromAction(() -> establishConnection(socket)));
    }

    @Override
    public void disconnect() {
        onDisconnect();
    }

    @Override
    public Single<List<Survey>> requestSurveys() {
        requestSurveysSubject = SingleSubject.create();

        return Completable.fromAction(() -> {
            if (transporter == null) {
                passErrorToMessageSubjects(new IllegalStateException());
                return;
            }

            BtMessage message = new BtMessage(BtMessage.Type.REQUEST_SYRVEYS, "");
            transporter.write(gson.toJson(message).getBytes());
        })
                .andThen(requestSurveysSubject);
    }

    @Override
    public Single<Survey> requestFilledSurvey(long surveyId) {
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
    public void onSocketAccept(BluetoothSocket socket) {
        establishConnection(socket);
    }

    private void establishConnection(BluetoothSocket socket) {
        killAcceptor();
        transporter = new Transporter(socket, this);
        setConnectionState(ConnectionState.CONNECTED);
        transporter.setState(connectionState);
        transporter.start();
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

    @Override
    public void onMessageObtain(String message) {
        try {
            BtMessage btMessage = gson.fromJson(message, BtMessage.class);
            switch (btMessage.getType()) {
                case REQUEST_SYRVEYS:
                    handleSurveysRequest();
                    break;
                case REQUEST_SURVEY:
                    break;
                case RESPONSE_SURVEYS:
                    handleSurveysResponse(btMessage.getContent());
                    break;
                case RESPONSE_SURVEY:
                    break;
            }
        } catch (JsonSyntaxException ex) {
            passErrorToMessageSubjects(ex);
        }
    }

    private void handleSurveysRequest() {
        if (transporter != null) {
            // TODO: temp logic
            List<SerializableWashSurvey> surveys = new ArrayList<>();
            MutableWashSurvey mutableWashSurvey = new MutableWashSurvey(1, SurveyType.SCHOOL_ACCREDITATION, AppRegion.FCM);

            mutableWashSurvey.setDate(new Date(1234567));
            mutableWashSurvey.setSchoolId("SCH001");
            mutableWashSurvey.setSchoolName("SCHOOL");
            surveys.add(new SerializableWashSurvey(mutableWashSurvey));

            mutableWashSurvey.setDate(new Date(1233678));
            mutableWashSurvey.setSchoolId("SCH002");
            mutableWashSurvey.setSchoolName("SCHOOL2");
            surveys.add(new SerializableWashSurvey(mutableWashSurvey));

            MutableWashSurvey mutableWashSurvey2 = new MutableWashSurvey(1, SurveyType.WASH, AppRegion.FCM);
            mutableWashSurvey.setDate(new Date(12312345));
            mutableWashSurvey.setSchoolId("SCH001");
            mutableWashSurvey.setSchoolName("SCHOOL");
            surveys.add(new SerializableWashSurvey(mutableWashSurvey2));

            ResponseWashSurveysBody responseSurveysBody = new ResponseWashSurveysBody(surveys);
            BtMessage message = new BtMessage(BtMessage.Type.RESPONSE_SURVEYS, gson.toJson(responseSurveysBody));
            transporter.write(gson.toJson(message).getBytes());
        }
    }

    private void handleSurveysResponse(String response) {
        if (requestSurveysSubject != null) {
            try {
                ResponseWashSurveysBody body = gson.fromJson(response, ResponseWashSurveysBody.class);
                requestSurveysSubject.onSuccess(body.getSurveys().stream().map(it -> (Survey) it).collect(Collectors.toList()));
            } catch (JsonSyntaxException ex) {
                passErrorToMessageSubjects(ex);
            }
            requestSurveysSubject = null;
        }
    }

    private void passErrorToMessageSubjects(Throwable throwable) {
        if (requestSurveysSubject != null) {
            requestSurveysSubject.onError(throwable);

        }
    }
}