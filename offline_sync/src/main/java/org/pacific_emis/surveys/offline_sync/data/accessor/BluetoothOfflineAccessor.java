package org.pacific_emis.surveys.offline_sync.data.accessor;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.pacific_emis.surveys.accreditation_core.data.data_source.AccreditationDataSource;
import org.pacific_emis.surveys.accreditation_core.data.model.AccreditationSurvey;
import org.pacific_emis.surveys.accreditation_core.data.model.MergeFieldsResult;
import org.pacific_emis.surveys.accreditation_core.data.model.mutable.MutableAccreditationSurvey;
import org.pacific_emis.surveys.accreditation_core.data.model.mutable.MutableObservationInfo;
import org.pacific_emis.surveys.accreditation_core.data.model.mutable.MutableObservationLogRecord;
import org.pacific_emis.surveys.core.data.exceptions.NotImplementedException;
import org.pacific_emis.surveys.core.data.files.FilesRepository;
import org.pacific_emis.surveys.core.data.model.ConflictResolveStrategy;
import org.pacific_emis.surveys.core.data.model.Photo;
import org.pacific_emis.surveys.core.data.model.Progress;
import org.pacific_emis.surveys.core.data.model.Survey;
import org.pacific_emis.surveys.core.data.model.SurveyState;
import org.pacific_emis.surveys.core.preferences.LocalSettings;
import org.pacific_emis.surveys.core.preferences.entities.AppRegion;
import org.pacific_emis.surveys.core.utils.CollectionUtils;
import org.pacific_emis.surveys.core.utils.TextUtil;
import org.pacific_emis.surveys.core.utils.VoidFunction;
import org.pacific_emis.surveys.offline_sync.data.bluetooth_threads.Acceptor;
import org.pacific_emis.surveys.offline_sync.data.bluetooth_threads.ConnectionState;
import org.pacific_emis.surveys.offline_sync.data.bluetooth_threads.Connector;
import org.pacific_emis.surveys.offline_sync.data.bluetooth_threads.Transporter;
import org.pacific_emis.surveys.offline_sync.data.exceptions.BluetoothGenericException;
import org.pacific_emis.surveys.offline_sync.data.model.AccreditationResponseSurveyBody;
import org.pacific_emis.surveys.offline_sync.data.model.BluetoothDeviceWrapper;
import org.pacific_emis.surveys.offline_sync.data.model.BtMessage;
import org.pacific_emis.surveys.offline_sync.data.model.Device;
import org.pacific_emis.surveys.offline_sync.data.model.RequestSurveyBody;
import org.pacific_emis.surveys.offline_sync.data.model.RequestSurveysBody;
import org.pacific_emis.surveys.offline_sync.data.model.ResponseAccreditationSurveysBody;
import org.pacific_emis.surveys.offline_sync.data.model.ResponseSurveyBody;
import org.pacific_emis.surveys.offline_sync.data.model.ResponseSurveysBody;
import org.pacific_emis.surveys.offline_sync.data.model.ResponseWashSurveysBody;
import org.pacific_emis.surveys.offline_sync.data.model.SyncNotification;
import org.pacific_emis.surveys.offline_sync.data.model.WashResponseSurveyBody;
import org.pacific_emis.surveys.offline_sync.domain.SyncNotifier;
import org.pacific_emis.surveys.wash_core.data.data_source.WashDataSource;
import org.pacific_emis.surveys.wash_core.data.model.WashSurvey;
import org.pacific_emis.surveys.wash_core.data.model.mutable.MutableWashSurvey;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

import static org.pacific_emis.surveys.core.utils.ViewUtils.createBitmapOptions;

public final class BluetoothOfflineAccessor implements OfflineAccessor, Transporter.Listener, Acceptor.OnSocketAcceptedListener {

    private static final String TAG = BluetoothOfflineAccessor.class.getName();
    private static final String MARK_FILE_NOT_EXIST = "File not exist";
    private static final String SEPARATOR_CREATE_USER = ", ";

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
    private final LocalSettings localSettings;
    private final AccreditationDataSource accreditationDataSource;
    private final WashDataSource washDataSource;
    private final FilesRepository filesRepository;
    private final SyncNotifier syncNotifier;

    private SyncUseCase syncUseCase;

    @Nullable
    private SingleSubject<List<Survey>> requestSurveysSubject;

    @Nullable
    private SingleSubject<Survey> requestFilledSurveySubject;

    @Nullable
    private SingleSubject<byte[]> requestPhotoSubject;

    private ConnectionState connectionState = ConnectionState.NONE;

    @Nullable
    private Acceptor acceptor;

    @Nullable
    private Connector connector;

    @Nullable
    private Transporter transporter;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public BluetoothOfflineAccessor(Context applicationContext,
                                    LocalSettings localSettings,
                                    AccreditationDataSource accreditationDataSource,
                                    WashDataSource washDataSource,
                                    FilesRepository filesRepository,
                                    SyncNotifier syncNotifier) {
        this.applicationContextRef = new WeakReference<>(applicationContext);
        this.localSettings = localSettings;
        this.accreditationDataSource = accreditationDataSource;
        this.washDataSource = washDataSource;
        this.filesRepository = filesRepository;
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
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public Single<List<Survey>> requestSurveys(String schoolId, String surveyTag) {
        requestSurveysSubject = SingleSubject.create();
        return send(new BtMessage(
                BtMessage.Type.REQUEST_SURVEYS,
                gson.toJson(new RequestSurveysBody(
                        schoolId,
                        localSettings.getCurrentAppRegion(),
                        localSettings.getSurveyTypeOrDefault(),
                        surveyTag
                ))
        ))
                .andThen(requestSurveysSubject);
    }

    @Override
    public Single<Survey> requestFilledSurvey(long surveyId) {
        requestFilledSurveySubject = SingleSubject.create();
        return send(new BtMessage(
                BtMessage.Type.REQUEST_FILLED_SURVEY,
                gson.toJson(new RequestSurveyBody(surveyId, localSettings.getSurveyTypeOrDefault()))
        ))
                .andThen(requestFilledSurveySubject);
    }

    private Completable send(BtMessage message) {
        return Completable.fromAction(() -> {
            if (transporter == null) {
                throw new BluetoothGenericException(new IllegalStateException());
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
        Log.d(TAG, "killAll");
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
                case END:
                    Schedulers.io().scheduleDirect(() -> {
                        Survey survey = syncUseCase.getTargetSurvey();

                        if (survey instanceof AccreditationSurvey) {
                            MutableAccreditationSurvey mutableAccreditationSurvey = new MutableAccreditationSurvey((AccreditationSurvey) survey);
                            mutableAccreditationSurvey.setState(SurveyState.MERGED);
                            accreditationDataSource.updateSurvey(mutableAccreditationSurvey);
                        } else if (survey instanceof WashSurvey) {
                            MutableWashSurvey mutableWashSurvey = new MutableWashSurvey((WashSurvey) survey);
                            mutableWashSurvey.setState(SurveyState.MERGED);
                            washDataSource.updateSurvey(mutableWashSurvey);
                        }

                        syncNotifier.notify(SyncNotification.just(SyncNotification.Type.DID_FINISH_SYNC));
                    });
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
            Log.d(TAG, "handleSurveysRequest - " + body);
            RequestSurveysBody request = gson.fromJson(body, RequestSurveysBody.class);
            String schoolId = request.getSchoolId();
            AppRegion region = request.getAppRegion();
            String surveyTag = request.getSurveyTag();

            VoidFunction<ResponseSurveysBody> onSuccess = v -> {
                BtMessage message = new BtMessage(BtMessage.Type.RESPONSE_SURVEYS, gson.toJson(v));
                String messageJson = gson.toJson(message);
                Log.d(TAG, "handleSurveysRequest send - " + messageJson);
                transporter.write(messageJson);
                syncNotifier.notify(SyncNotification.just(SyncNotification.Type.DID_SEND_AVAILABLE_SURVEYS));
            };

            switch (request.getSurveyType()) {
                case SCHOOL_ACCREDITATION:
                    compositeDisposable.add(
                            accreditationDataSource.loadSurveys(schoolId, region, surveyTag)
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
                            washDataSource.loadSurveys(schoolId, region, surveyTag)
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
            Log.d(TAG, "handleSurveysResponse - " + response);
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
            Log.d(TAG, "handleFilledSurveyRequest - " + body);
            RequestSurveyBody request = gson.fromJson(body, RequestSurveyBody.class);

            VoidFunction<ResponseSurveyBody> onSuccess = v -> {
                BtMessage message = new BtMessage(BtMessage.Type.RESPONSE_FILLED_SURVEY, gson.toJson(v));
                String messageJson = gson.toJson(message);
                Log.d(TAG, "handleFilledSurveyRequest send - " + messageJson);
                transporter.write(messageJson);
                syncUseCase.setTargetSurvey(v.getSurvey());
                syncNotifier.notify(SyncNotification.just(SyncNotification.Type.DID_SEND_SURVEY));
                syncNotifier.notify(SyncNotification.withValue(SyncNotification.Type.WILL_SEND_PHOTOS, v.getSurvey().getPhotosCount()));
            };

            switch (request.getSurveyType()) {
                case SCHOOL_ACCREDITATION:
                    compositeDisposable.add(
                            accreditationDataSource.loadSurvey(localSettings.getCurrentAppRegion(), request.getSurveyId())
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
                            washDataSource.loadSurvey(localSettings.getCurrentAppRegion(), request.getSurveyId())
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
            Log.d(TAG, "handleFilledSurveyResponse - " + response);
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

        Log.d(TAG, "passErrorToMessageSubjects - " + throwable.getMessage());
        syncNotifier.notify(SyncNotification.error(throwable));
    }

    @Override
    public Single<Survey> mergeSurveys(Survey targetSurvey, Survey externalSurvey, ConflictResolveStrategy strategy) {
        Single<Survey> merge;

        if (targetSurvey instanceof AccreditationSurvey && externalSurvey instanceof AccreditationSurvey) {
            merge = mergeAccreditationSurveys((AccreditationSurvey) targetSurvey, (AccreditationSurvey) externalSurvey, strategy);
        } else if (targetSurvey instanceof WashSurvey && externalSurvey instanceof WashSurvey) {
            merge = mergeWashSurveys((WashSurvey) targetSurvey, (WashSurvey) externalSurvey, strategy);
        } else {
            return Single.error(new NotImplementedException());
        }

        return merge
                .flatMap(survey -> Single.fromCallable(() -> {
                    syncNotifier.notify(SyncNotification.just(SyncNotification.Type.DID_FINISH_SYNC));

                    if (transporter != null) {
                        transporter.write(gson.toJson(new BtMessage(BtMessage.Type.END, null)));
                    }

                    return survey;
                }));
    }

    private Single<Survey> mergeAccreditationSurveys(AccreditationSurvey targetSurvey,
                                                     AccreditationSurvey externalSurvey,
                                                     ConflictResolveStrategy strategy) {
        return Single.fromCallable(() -> {
            MutableAccreditationSurvey mutableTargetSurvey = (MutableAccreditationSurvey) targetSurvey;
            final MergeFieldsResult mergeFieldsResult = mutableTargetSurvey.merge(externalSurvey, strategy);
            accreditationDataSource.updateSurvey(mutableTargetSurvey);
            int photosCount = (int) mergeFieldsResult.getAnswers()
                    .stream()
                    .mapToLong(a -> nonNullWrapPhotos(a.getPhotos()).size())
                    .sum();
            syncNotifier.notify(SyncNotification.withValue(SyncNotification.Type.WILL_SAVE_PHOTOS, photosCount));
            Log.d(TAG, "did merge accreditation with photos = " + photosCount);
            return mergeFieldsResult;
        })
                .flatMap(mergeFieldsResult -> updateMergedSurveyFields(mergeFieldsResult)
                        .andThen(Single.just(mergeFieldsResult.getAnswers())))
                .flatMapCompletable(changedAnswers -> Flowable.range(0, changedAnswers.size())
                        .concatMap(index -> accreditationDataSource.updateAnswer(changedAnswers.get(index))
                                .flattenAsFlowable(a -> nonNullWrapPhotos(a.getPhotos()))
                                .concatMap(this::acquirePhoto)
                        )
                        .ignoreElements()
                )
                .andThen(accreditationDataSource.loadSurvey(localSettings.getCurrentAppRegion(), targetSurvey.getId()))
                .cast(MutableAccreditationSurvey.class)
                .flatMap(updatedSurvey -> updateAccreditationSurveyState(updatedSurvey, externalSurvey));
    }

    private Completable updateMergedSurveyFields(@NonNull MergeFieldsResult mergeFieldsResult) {
        return updateMergedCategoryObservationInfo(mergeFieldsResult.getObservationInfoList())
                .andThen(updateMergedCategoryObservationLog(mergeFieldsResult));
    }

    private Completable updateMergedCategoryObservationInfo(@NonNull List<Pair<Long, MutableObservationInfo>> updatePairs) {
        return Observable.fromIterable(updatePairs)
                .concatMapCompletable(pair -> {
                    final long categoryId = pair.first;
                    final MutableObservationInfo info = pair.second;
                    return accreditationDataSource.updateObservationInfo(info, categoryId);
                });
    }

    private Completable updateMergedCategoryObservationLog(@NonNull MergeFieldsResult mergeFieldsResult) {
        return Observable.fromIterable(CollectionUtils.toIterable(mergeFieldsResult.getAddedLogRecords()))
                .concatMapCompletable(pair -> {
                    final long categoryId = pair.first;
                    final List<MutableObservationLogRecord> addedLogs = pair.second;
                    return accreditationDataSource.createLogRecords(categoryId, addedLogs);
                })
                .andThen(Observable.fromIterable(CollectionUtils.toIterable(mergeFieldsResult.getUpdatedLogRecords())))
                .concatMapCompletable(pair -> {
                    final List<MutableObservationLogRecord> updatedLogs = pair.second;
                    return Observable.fromIterable(updatedLogs)
                            .concatMapCompletable(accreditationDataSource::updateObservationLogRecord);
                });
    }

    private Single<MutableAccreditationSurvey> updateAccreditationSurveyState(MutableAccreditationSurvey mutableAccreditationSurvey,
                                                                              AccreditationSurvey externalSurvey) {
        return Single.fromCallable(() -> {
            Progress progress = mutableAccreditationSurvey.calculateProgress();
            mutableAccreditationSurvey.setState(progress.isFinished() ? SurveyState.COMPLETED : SurveyState.NOT_COMPLETED);
            mutableAccreditationSurvey.setLastEditedUser(externalSurvey.getLastEditedUser());
            mutableAccreditationSurvey.setCreateUser(getUpdatedCreateUser(mutableAccreditationSurvey, externalSurvey));
            accreditationDataSource.updateSurvey(mutableAccreditationSurvey);
            return mutableAccreditationSurvey;
        });
    }

    @NonNull
    private String getUpdatedCreateUser(Survey localSurvey, Survey externalSurvey) {
        String existingCreateUser = localSurvey.getCreateUser();
        String existingExternalUser = externalSurvey.getCreateUser();
        String updatedCreateUser = "";

        if (existingCreateUser != null) {
            updatedCreateUser += existingCreateUser;
        }

        if (existingExternalUser != null && !existingExternalUser.contains(existingCreateUser) && !existingCreateUser.contains(existingExternalUser)) {
            updatedCreateUser += SEPARATOR_CREATE_USER + externalSurvey.getCreateUser();
        } else if(existingExternalUser.contains(existingCreateUser)) {
            updatedCreateUser = externalSurvey.getCreateUser();
        }

        return updatedCreateUser;
    }

    private Single<Survey> mergeWashSurveys(WashSurvey targetSurvey, WashSurvey externalSurvey, ConflictResolveStrategy strategy) {
        return Single.fromCallable(() -> {
            MutableWashSurvey mutableTargetSurvey = (MutableWashSurvey) targetSurvey;
            List<org.pacific_emis.surveys.wash_core.data.model.mutable.MutableAnswer> answers =
                    mutableTargetSurvey.merge(externalSurvey, strategy);
            int photosCount = (int) answers.stream().mapToLong(a -> nonNullWrapPhotos(a.getPhotos()).size()).sum();
            syncNotifier.notify(SyncNotification.withValue(SyncNotification.Type.WILL_SAVE_PHOTOS, photosCount));
            Log.d(TAG, "did merge wash with photos = " + photosCount);
            return Pair.create(mutableTargetSurvey, answers);
        })
                .flatMap(pair -> {
                    washDataSource.updateSurvey(pair.first);
                    return Single.just(pair.second);
                })
                .flatMapCompletable(changedAnswers -> Flowable.range(0, changedAnswers.size())
                        .concatMap(index -> washDataSource.updateAnswer(changedAnswers.get(index))
                                .flattenAsFlowable(a -> nonNullWrapPhotos(a.getPhotos()))
                                .concatMap(this::acquirePhoto)
                        )
                        .ignoreElements()
                )
                .andThen(washDataSource.loadSurvey(localSettings.getCurrentAppRegion(), targetSurvey.getId()))
                .cast(MutableWashSurvey.class)
                .flatMap(updatedSurvey -> updateWashSurveyState(updatedSurvey, externalSurvey));
    }

    private Single<MutableWashSurvey> updateWashSurveyState(MutableWashSurvey mutableWashSurvey,
                                                            WashSurvey externalSurvey) {
        return Single.fromCallable(() -> {
            Progress progress = mutableWashSurvey.calculateProgress();
            mutableWashSurvey.setState(progress.isFinished() ? SurveyState.COMPLETED : SurveyState.NOT_COMPLETED);
            mutableWashSurvey.setLastEditedUser(externalSurvey.getLastEditedUser());
            mutableWashSurvey.setCreateUser(getUpdatedCreateUser(mutableWashSurvey, externalSurvey));
            washDataSource.updateSurvey(mutableWashSurvey);
            return mutableWashSurvey;
        });
    }

    private List<? extends Photo> nonNullWrapPhotos(@Nullable List<? extends Photo> photos) {
        return photos == null ? Collections.emptyList() : photos;
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
                    syncNotifier.notify(SyncNotification.just(SyncNotification.Type.DID_SAVE_PHOTO));
                }))
                .toFlowable();
    }

    private void savePhotoBytesToFile(String fileName, byte[] bytes) {
        try {
            byte[] decodedBytes = Base64.decode(bytes, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length, createBitmapOptions());

            if (bitmap == null && new String(bytes).equals(MARK_FILE_NOT_EXIST)) {
                return; // file not exist on other device, so this is handled error case
            }

            File photoFile = filesRepository.createEmptyImageFile(fileName);

            if (photoFile == null) {
                return;
            }

            FileOutputStream fos = new FileOutputStream(photoFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
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
            transporter.write(MARK_FILE_NOT_EXIST);
        }

        syncNotifier.notify(SyncNotification.just(SyncNotification.Type.DID_SEND_PHOTO));
    }

    @Nullable
    @Override
    public Device getCurrentConnectedDevice() {
        if (transporter == null) {
            return null;
        }

        return new BluetoothDeviceWrapper(transporter.getDevice());
    }
}