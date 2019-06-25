package fm.doe.national.offline_sync.data.accessor;

import android.content.Context;
import android.content.Intent;

import java.util.List;

import fm.doe.national.core.data.model.Survey;
import fm.doe.national.offline_sync.data.bluetooth_threads.ConnectionState;
import fm.doe.national.offline_sync.data.model.Device;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.functions.Action;
import io.reactivex.subjects.Subject;

public interface OfflineAccessor {

    Subject<List<Device>> getDevicesSubject();

    Subject<ConnectionState> getConnectionStateSubject();

    Subject<Action> getDiscoverableRequestSubject();

    Subject<Action> getPermissionsRequestSubject();

    void discoverDevices();

    void stopDiscoverDevices();

    void becomeAvailableToConnect();

    void becomeUnavailableToConnect();

    Completable connect(Device device);

    void disconnect();

    Single<List<Survey>> requestSurveys(String schoolId);

    Single<Survey> requestFilledSurvey(long surveyId);

    void onBroadcastReceive(Context context, Intent intent);

}
