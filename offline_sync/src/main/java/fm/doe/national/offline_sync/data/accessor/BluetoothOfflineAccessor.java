package fm.doe.national.offline_sync.data.accessor;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import fm.doe.national.accreditation_core.data.data_source.AccreditationDataSource;
import fm.doe.national.accreditation_core.data.model.AccreditationSurvey;
import fm.doe.national.accreditation_core.data.model.mutable.MutableAccreditationSurvey;
import fm.doe.national.core.data.exceptions.NotImplementedException;
import fm.doe.national.core.data.files.PicturesRepository;
import fm.doe.national.core.data.model.ConflictResolveStrategy;
import fm.doe.national.core.data.model.Photo;
import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.preferences.GlobalPreferences;
import fm.doe.national.core.preferences.entities.AppRegion;
import fm.doe.national.core.utils.TextUtil;
import fm.doe.national.core.utils.VoidFunction;
import fm.doe.national.offline_sync.data.bluetooth_threads.Acceptor;
import fm.doe.national.offline_sync.data.bluetooth_threads.ConnectionState;
import fm.doe.national.offline_sync.data.bluetooth_threads.Connector;
import fm.doe.national.offline_sync.data.bluetooth_threads.Transporter;
import fm.doe.national.offline_sync.data.exceptions.BluetoothGenericException;
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
import fm.doe.national.offline_sync.data.model.SyncNotification;
import fm.doe.national.offline_sync.data.model.WashResponseSurveyBody;
import fm.doe.national.offline_sync.domain.SyncNotifier;
import fm.doe.national.wash_core.data.data_source.WashDataSource;
import fm.doe.national.wash_core.data.model.WashSurvey;
import fm.doe.national.wash_core.data.model.mutable.MutableWashSurvey;
import io.reactivex.Completable;
import io.reactivex.Flowable;
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

import static fm.doe.national.core.utils.ViewUtils.createBitmapOptions;

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
    private final AccreditationDataSource accreditationDataSource;
    private final WashDataSource washDataSource;
    private final PicturesRepository picturesRepository;
    private final SyncNotifier syncNotifier;

    private SyncUseCase syncUseCase;

    @Nullable
    private SingleSubject<List<Survey>> requestSurveysSubject;

    @Nullable
    private SingleSubject<Survey> requestFilledSurveySubject;

    @Nullable
    private SingleSubject<byte[]> requestPhotoSubject;

    @Nullable
    private CompletableSubject pushSurveySubject;

    private ConnectionState connectionState = ConnectionState.NONE;

    @Nullable
    private Acceptor acceptor;

    @Nullable
    private Connector connector;

    @Nullable
    private Transporter transporter;

    public BluetoothOfflineAccessor(Context applicationContext,
                                    GlobalPreferences globalPreferences,
                                    AccreditationDataSource accreditationDataSource,
                                    WashDataSource washDataSource,
                                    PicturesRepository picturesRepository,
                                    SyncNotifier syncNotifier) {
        this.applicationContextRef = new WeakReference<>(applicationContext);
        this.globalPreferences = globalPreferences;
        this.accreditationDataSource = accreditationDataSource;
        this.washDataSource = washDataSource;
        this.picturesRepository = picturesRepository;
        this.syncNotifier = syncNotifier;
    }

    @Override
    public void setSyncUseCase(SyncUseCase syncUseCase) {
        this.syncUseCase = syncUseCase;
    }

    @Override
    public Observable<List<Device>> getDevicesObservable() {
        return devicesSubject;
    }

    @Override
    public Observable<ConnectionState> getConnectionStateObservable() {
        return connectionStateSubject;
    }

    @Override
    public Observable<Action> getDiscoverableRequestObservable() {
        return discoverableRequestSubject;
    }

    @Override
    public Observable<Action> getPermissionsRequestObservable() {
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
    public Completable becomeAvailableToConnect() {
        killAll();

        Context appContext = applicationContextRef.get();

        if (appContext == null) {
            return Completable.error(new IllegalStateException());
        }

        CompletableSubject completableSubject = CompletableSubject.create();

        doWithBluetoothPermissions(() -> doInDiscoverableMode(() -> {
            acceptor = new Acceptor(appContext, bluetoothAdapter, this);
            acceptor.start();
            setConnectionState(ConnectionState.LISTENING);
            completableSubject.onComplete();
        }));

        return completableSubject;
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
            return CompletableSubject.error(new BluetoothGenericException(new IllegalStateException()));
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
                passErrorToMessageSubjects(new BluetoothGenericException(new IllegalStateException()));
                return;
            }

            transporter.write(gson.toJson(message));
        });
    }

    @Override
    public void onBroadcastReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action == null) {
            return;
        }

        switch (action) {
            case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                devicesCache.clear();
                break;
            case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                devicesSubject.onNext(devicesCache.stream().map(BluetoothDeviceWrapper::new).collect(Collectors.toList()));
                break;
            case BluetoothDevice.ACTION_FOUND:
                devicesCache.add(intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE));
                break;
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
    public void onMessageObtain(byte[] bytes) {
        try {
            BtMessage btMessage = gson.fromJson(new String(bytes), BtMessage.class);
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
                case REQUEST_PHOTO:
                    handlePhotoRequest(btMessage.getContent());
                    break;
                case PUSH_SURVEY:
                    handlePushSurvey(btMessage.getContent());
                    break;
                case END:
                    if (pushSurveySubject != null) {
                        pushSurveySubject.onComplete();
                    }

                    syncNotifier.notify(new SyncNotification(SyncNotification.Type.DID_FINISH_SYNC));
                    break;
            }
        } catch (JsonSyntaxException ex) {
            try {
                handlePhotoResponse(bytes);
            } catch (IllegalStateException illegalStateException) {
                passErrorToMessageSubjects(ex);
            }
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
                syncNotifier.notify(new SyncNotification(SyncNotification.Type.DID_SEND_AVAILABLE_SURVEYS));
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
                syncUseCase.setTargetSurvey(v.getSurvey());
                syncNotifier.notify(new SyncNotification(SyncNotification.Type.DID_SEND_SURVEY));
                syncNotifier.notify(new SyncNotification(SyncNotification.Type.WILL_SEND_PHOTOS, v.getSurvey().getPhotosCount()));
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
            AccreditationResponseSurveyBody accreditationBody = gson.fromJson(json, AccreditationResponseSurveyBody.class);

            // fix for https://github.com/google/gson/issues/61
            if (accreditationBody.getSurvey() == null) {
                throw new JsonSyntaxException("Required field not present");
            }

            return accreditationBody;
        } catch (JsonSyntaxException ex) {
            try {
                return gson.fromJson(json, WashResponseSurveyBody.class);
            } catch (JsonSyntaxException otherEx) {
                return null;
            }
        }
    }

    private void passErrorToMessageSubjects(Throwable throwable) {
        Throwable error = new BluetoothGenericException(throwable);

        if (requestSurveysSubject != null) {
            requestSurveysSubject.onError(error);
        }

        if (requestFilledSurveySubject != null) {
            requestFilledSurveySubject.onError(error);
        }

        if (requestPhotoSubject != null) {
            requestPhotoSubject.onError(error);
        }
    }

    @Override
    public Single<Survey> mergeSurveys(Survey targetSurvey, Survey externalSurvey, ConflictResolveStrategy strategy) {
        if (targetSurvey instanceof AccreditationSurvey && externalSurvey instanceof AccreditationSurvey) {
            return mergeAccreditationSurveys((AccreditationSurvey) targetSurvey, (AccreditationSurvey) externalSurvey, strategy);
        }

        if (targetSurvey instanceof WashSurvey && externalSurvey instanceof WashSurvey) {
            return mergeWashSurveys((WashSurvey) targetSurvey, (WashSurvey) externalSurvey, strategy);
        }

        return Single.error(new NotImplementedException());
    }

    private Single<Survey> mergeAccreditationSurveys(AccreditationSurvey targetSurvey,
                                                     AccreditationSurvey externalSurvey,
                                                     ConflictResolveStrategy strategy) {
        return Single.fromCallable(() -> {
            MutableAccreditationSurvey mutableTargetSurvey = (MutableAccreditationSurvey) targetSurvey;
            return mutableTargetSurvey.merge(externalSurvey, strategy);
        })
                .flatMapCompletable(changedAnswers -> Flowable.range(0, changedAnswers.size())
                        .concatMap(index -> accreditationDataSource.updateAnswer(changedAnswers.get(index))
                                .flattenAsFlowable(a -> getPhotosWithNotification(a.getPhotos()))
                                .concatMap(this::acquirePhoto)
                        )
                        .ignoreElements()
                )
                .andThen(accreditationDataSource.loadSurvey(targetSurvey.getId()));
    }

    private List<? extends Photo> getPhotosWithNotification(List<? extends Photo> photos) {
        if (photos == null) {
            photos = new ArrayList<>();
        }

        syncNotifier.notify(new SyncNotification(SyncNotification.Type.WILL_SAVE_PHOTOS, photos.size()));
        return photos;
    }

    private Single<Survey> mergeWashSurveys(WashSurvey targetSurvey, WashSurvey externalSurvey, ConflictResolveStrategy strategy) {
        return Single.fromCallable(() -> {
            MutableWashSurvey mutableTargetSurvey = (MutableWashSurvey) targetSurvey;
            return mutableTargetSurvey.merge(externalSurvey, strategy);
        })
                .flatMapCompletable(changedAnswers -> Flowable.range(0, changedAnswers.size())
                        .concatMap(index -> washDataSource.updateAnswer(changedAnswers.get(index))
                                .flattenAsFlowable(a -> getPhotosWithNotification(a.getPhotos()))
                                .concatMap(this::acquirePhoto)
                        )
                        .ignoreElements()
                )
                .andThen(washDataSource.loadSurvey(targetSurvey.getId()));
    }

    private Flowable<Object> acquirePhoto(Photo photo) {
        String path = photo.getLocalPath();
        return requestPhoto(path)
                .flatMapCompletable(photoBytes -> Completable.fromAction(() -> {
                    requestPhotoSubject = null;

                    if (photoBytes.length == 0) {
                        return;
                    }

                    savePhotoBytesToFile(TextUtil.getFileNameWithoutExtension(path), photoBytes);
                }))
                .toFlowable();
    }

    private void savePhotoBytesToFile(String fileName, byte[] bytes) {
        File photoFile;

        try {
            photoFile = picturesRepository.createEmptyFile(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BluetoothGenericException(e);
        }

        if (photoFile == null) {
            return;
        }

        try (FileOutputStream fos = new FileOutputStream(photoFile)) {
            byte[] decodedBytes = Base64.decode(bytes, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length, createBitmapOptions());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new BluetoothGenericException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BluetoothGenericException(e);
        }
    }

    private Single<byte[]> requestPhoto(String path) {
        if (transporter == null) {
            return Single.just(new byte[]{});
        }

        requestPhotoSubject = SingleSubject.create();
        return send(new BtMessage(BtMessage.Type.REQUEST_PHOTO, path))
                .andThen(requestPhotoSubject);
    }

    private void handlePhotoResponse(byte[] bytes) {
        if (requestPhotoSubject == null) {
            throw new BluetoothGenericException(new IllegalStateException());
        }

        requestPhotoSubject.onSuccess(bytes);
    }

    private void handlePhotoRequest(String path) {
        if (transporter == null) {
            return;
        }

        File file = new File(path);
        if (file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(path, createBitmapOptions());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            transporter.write(Base64.encode(bytes, Base64.DEFAULT));
        } else {
            transporter.write("File not exist");
        }

        syncNotifier.notify(new SyncNotification(SyncNotification.Type.DID_SEND_PHOTO));
    }

    @Override
    public Completable pushSurvey(Survey mergedSurvey) {
        pushSurveySubject = CompletableSubject.create();
        return Completable.fromAction(() -> {
            if (transporter == null) {
                throw new BluetoothGenericException(new IllegalStateException());
            }
            BtMessage message = new BtMessage(BtMessage.Type.PUSH_SURVEY, gson.toJson(mergedSurvey));
            transporter.write(gson.toJson(message));
        })
                .andThen(pushSurveySubject);

    }

    private void handlePushSurvey(String content) {
        if (transporter == null) {
            return;
        }

        Survey externalSurvey = tryParseSurveyBodyJson(content);

        if (externalSurvey == null) {
            return;
        }

        compositeDisposable.add(
                mergeSurveys(syncUseCase.getTargetSurvey(), externalSurvey, ConflictResolveStrategy.THEIRS)
                        .subscribe(survey -> {
                            transporter.write(gson.toJson(new BtMessage(BtMessage.Type.END, null)));
                            syncNotifier.notify(new SyncNotification(SyncNotification.Type.DID_FINISH_SYNC));
                        }, Throwable::printStackTrace)
        );

    }

    @Nullable
    private Survey tryParseSurveyBodyJson(String json) {
        try {
            return gson.fromJson(json, MutableAccreditationSurvey.class);
        } catch (JsonSyntaxException ex) {
            try {
                return gson.fromJson(json, MutableWashSurvey.class);
            } catch (JsonSyntaxException otherEx) {
                return null;
            }
        }
    }
}