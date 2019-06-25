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
import java.util.List;
import java.util.stream.Collectors;

import fm.doe.national.accreditation_core.data.model.AccreditationSurvey;
import fm.doe.national.accreditation_core.data.model.mutable.MutableAccreditationSurvey;
import fm.doe.national.core.data.data_source.DataSource;
import fm.doe.national.core.data.exceptions.NotImplementedException;
import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.preferences.GlobalPreferences;
import fm.doe.national.core.preferences.entities.AppRegion;
import fm.doe.national.core.utils.VoidFunction;
import fm.doe.national.offline_sync.data.bluetooth_threads.Acceptor;
import fm.doe.national.offline_sync.data.bluetooth_threads.ConnectionState;
import fm.doe.national.offline_sync.data.bluetooth_threads.Connector;
import fm.doe.national.offline_sync.data.bluetooth_threads.Transporter;
import fm.doe.national.offline_sync.data.model.AccreditationResponseSurveyBody;
import fm.doe.national.offline_sync.data.model.BluetoothDeviceWrapper;
import fm.doe.national.offline_sync.data.model.BtMessage;
import fm.doe.national.offline_sync.data.model.Device;
import fm.doe.national.offline_sync.data.model.RequestSurveyBody;
import fm.doe.national.offline_sync.data.model.RequestSurveysBody;
import fm.doe.national.offline_sync.data.model.ResponseAccreditationSurveysBody;
import fm.doe.national.offline_sync.data.model.ResponseSurveyBody;
import fm.doe.national.offline_sync.data.model.ResponseSurveysBody;
import fm.doe.national.offline_sync.data.model.ResponseWashSurveysBody;
import fm.doe.national.offline_sync.data.model.WashResponseSurveyBody;
import fm.doe.national.wash_core.data.model.WashSurvey;
import fm.doe.national.wash_core.data.model.mutable.MutableWashSurvey;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
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
    private final GlobalPreferences globalPreferences;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final DataSource accreditationDataSource;
    private final DataSource washDataSource;

    @Nullable
    private SingleSubject<List<Survey>> requestSurveysSubject;

    @Nullable
    private SingleSubject<Survey> requestFilledSurveySubject;

    private ConnectionState connectionState = ConnectionState.NONE;

    @Nullable
    private Acceptor acceptor;

    @Nullable
    private Connector connector;

    @Nullable
    private Transporter transporter;

    public BluetoothOfflineAccessor(Context applicationContext,
                                    GlobalPreferences globalPreferences,
                                    DataSource accreditationDataSource,
                                    DataSource washDataSource) {
        this.applicationContextRef = new WeakReference<>(applicationContext);
        this.globalPreferences = globalPreferences;
        this.accreditationDataSource = accreditationDataSource;
        this.washDataSource = washDataSource;
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
        compositeDisposable.dispose();
    }

    @Override
    public Single<List<Survey>> requestSurveys(String schoolId) {
        requestSurveysSubject = SingleSubject.create();
        return send(new BtMessage(
                BtMessage.Type.REQUEST_SURVEYS,
                gson.toJson(new RequestSurveysBody(
                        schoolId,
                        globalPreferences.getAppRegion(),
                        globalPreferences.getSurveyTypeOrDefault()
                ))
        ))
                .andThen(requestSurveysSubject);
    }

    @Override
    public Single<Survey> requestFilledSurvey(long surveyId) {
        requestFilledSurveySubject = SingleSubject.create();
        return send(new BtMessage(
                BtMessage.Type.REQUEST_FILLED_SURVEY,
                gson.toJson(new RequestSurveyBody(surveyId, globalPreferences.getSurveyTypeOrDefault()))
        ))
                .andThen(requestFilledSurveySubject);
    }

    private Completable send(BtMessage message) {
        return Completable.fromAction(() -> {
            if (transporter == null) {
                passErrorToMessageSubjects(new IllegalStateException());
                return;
            }

            transporter.write(gson.toJson(message));
        });
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
                case REQUEST_SURVEYS:
                    handleSurveysRequest(btMessage.getContent());
                    break;
                case REQUEST_FILLED_SURVEY:
                    handleFilledSurveyRequest(btMessage.getContent());
                    break;
                case RESPONSE_SURVEYS:
                    handleSurveysResponse(btMessage.getContent());
                    break;
                case RESPONSE_FILLED_SURVEY:
                    handleFilledSurveyResponse(btMessage.getContent());
                    break;
            }
        } catch (JsonSyntaxException ex) {
            passErrorToMessageSubjects(ex);
        }
    }

    private void handleSurveysRequest(String body) {
        if (transporter != null) {
            RequestSurveysBody request = gson.fromJson(body, RequestSurveysBody.class);
            String schoolId = request.getSchoolId();
            AppRegion region = request.getAppRegion();

            VoidFunction<ResponseSurveysBody> onSuccess = v -> {
                BtMessage message = new BtMessage(BtMessage.Type.RESPONSE_SURVEYS, gson.toJson(v));
                transporter.write(gson.toJson(message));
            };

            switch (request.getSurveyType()) {
                case SCHOOL_ACCREDITATION:
                    compositeDisposable.add(
                            accreditationDataSource.loadSurveys(schoolId, region)
                                    .flatMapObservable(Observable::fromIterable)
                                    .cast(AccreditationSurvey.class)
                                    .map(MutableAccreditationSurvey::new)
                                    .toList()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(Schedulers.io())
                                    .subscribe(
                                            surveys -> onSuccess.apply(new ResponseAccreditationSurveysBody(surveys)),
                                            Throwable::printStackTrace
                                    )
                    );
                    break;
                case WASH:
                    compositeDisposable.add(
                            washDataSource.loadSurveys(schoolId, region)
                                    .flatMapObservable(Observable::fromIterable)
                                    .cast(WashSurvey.class)
                                    .map(MutableWashSurvey::new)
                                    .toList()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(Schedulers.io())
                                    .subscribe(
                                            surveys -> onSuccess.apply(new ResponseWashSurveysBody(surveys)),
                                            Throwable::printStackTrace
                                    )
                    );
                    break;
                default:
                    throw new NotImplementedException();
            }
        }
    }

    private void handleSurveysResponse(String response) {
        if (requestSurveysSubject != null) {
            ResponseSurveysBody body = tryParseResponseSurveysBodyJson(response);

            if (body != null) {
                requestSurveysSubject.onSuccess(body.getSurveys().stream().map(it -> (Survey) it).collect(Collectors.toList()));
            } else {
                passErrorToMessageSubjects(new IllegalStateException("Failed to parse server device response"));
            }

            requestSurveysSubject = null;
        }
    }

    private void handleFilledSurveyRequest(String body) {
        if (transporter != null) {
            RequestSurveyBody request = gson.fromJson(body, RequestSurveyBody.class);

            VoidFunction<ResponseSurveyBody> onSuccess = v -> {
                BtMessage message = new BtMessage(BtMessage.Type.RESPONSE_FILLED_SURVEY, gson.toJson(v));
                transporter.write(gson.toJson(message));
            };

            switch (request.getSurveyType()) {
                case SCHOOL_ACCREDITATION:
                    compositeDisposable.add(
                            accreditationDataSource.loadSurvey(request.getSurveyId())
                                    .cast(AccreditationSurvey.class)
                                    .map(MutableAccreditationSurvey::new)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(Schedulers.io())
                                    .subscribe(
                                            survey -> onSuccess.apply(new AccreditationResponseSurveyBody(survey)),
                                            Throwable::printStackTrace
                                    )
                    );
                    break;
                case WASH:
                    compositeDisposable.add(
                            washDataSource.loadSurvey(request.getSurveyId())
                                    .cast(WashSurvey.class)
                                    .map(MutableWashSurvey::new)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(Schedulers.io())
                                    .subscribe(
                                            survey -> onSuccess.apply(new WashResponseSurveyBody(survey)),
                                            Throwable::printStackTrace
                                    )
                    );
                    break;
                default:
                    throw new NotImplementedException();
            }
        }
    }

    private void handleFilledSurveyResponse(String response) {
        if (requestFilledSurveySubject != null) {
            ResponseSurveyBody body = tryParseResponseSurveyBodyJson(response);

            if (body != null) {
                requestFilledSurveySubject.onSuccess(body.getSurvey());
            } else {
                passErrorToMessageSubjects(new IllegalStateException("Failed to parse server device response"));
            }

            requestFilledSurveySubject = null;
        }
    }

    @Nullable
    private ResponseSurveysBody tryParseResponseSurveysBodyJson(String json) {
        try {
            return gson.fromJson(json, ResponseAccreditationSurveysBody.class);
        } catch (JsonSyntaxException ex) {
            try {
                return gson.fromJson(json, ResponseWashSurveysBody.class);
            } catch (JsonSyntaxException otherEx) {
                return null;
            }
        }
    }

    @Nullable
    private ResponseSurveyBody tryParseResponseSurveyBodyJson(String json) {
        try {
            return gson.fromJson(json, AccreditationResponseSurveyBody.class);
        } catch (JsonSyntaxException ex) {
            try {
                return gson.fromJson(json, WashResponseSurveyBody.class);
            } catch (JsonSyntaxException otherEx) {
                return null;
            }
        }
    }

    private void passErrorToMessageSubjects(Throwable throwable) {
        if (requestSurveysSubject != null) {
            requestSurveysSubject.onError(throwable);
        }

        if (requestFilledSurveySubject != null) {
            requestFilledSurveySubject.onError(throwable);
        }
    }

    @Override
    public Completable mergeSurveys(Survey targetSurvey, Survey externalSurvey) {
        // TODO: will be implemented in next feature
//        if (targetSurvey instanceof AccreditationSurvey && externalSurvey instanceof AccreditationSurvey) {
//            return mergeAccreditationSurveys((AccreditationSurvey) targetSurvey, (AccreditationSurvey) externalSurvey);
//        }
//
//        if (targetSurvey instanceof WashSurvey && externalSurvey instanceof WashSurvey) {
//            return mergeWashSurveys((WashSurvey) targetSurvey, (WashSurvey) externalSurvey);
//        }

        return Completable.error(new NotImplementedException());
    }

    // TODO: will be implemented in next feature
//    private Completable mergeAccreditationSurveys(AccreditationSurvey targetSurvey, AccreditationSurvey externalSurvey) {
//        return Completable.fromAction(() -> {
//        });
//    }
//
//    private Completable mergeWashSurveys(WashSurvey targetSurvey, WashSurvey externalSurvey) {
//        return Completable.fromAction(() -> {
//        });
//    }
}