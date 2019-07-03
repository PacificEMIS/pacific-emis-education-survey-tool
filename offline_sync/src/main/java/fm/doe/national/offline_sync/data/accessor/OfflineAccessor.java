package fm.doe.national.offline_sync.data.accessor;

import android.content.Context;
import android.content.Intent;

import java.util.List;

import fm.doe.national.core.data.model.Survey;
import fm.doe.national.offline_sync.data.bluetooth_threads.ConnectionState;
import fm.doe.national.offline_sync.data.model.Device;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Action;

public interface OfflineAccessor {

    Observable<List<Device>> getDevicesObservable();

    Observable<ConnectionState> getConnectionStateObservable();

    Observable<Action> getDiscoverableRequestObservable();

    Observable<Action> getPermissionsRequestObservable();

    void discoverDevices();

    void stopDiscoverDevices();

    Completable becomeAvailableToConnect();

    void becomeUnavailableToConnect();

    Completable connect(Device device);

    void disconnect();

    Single<List<Survey>> requestSurveys(String schoolId);

    Single<Survey> requestFilledSurvey(long surveyId);

    void onBroadcastReceive(Context context, Intent intent);

    Completable mergeSurveys(Survey targetSurvey, Survey externalSurvey);
}
