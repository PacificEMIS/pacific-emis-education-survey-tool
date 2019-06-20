package fm.doe.national.offline_sync.data.accessor;

import android.content.Context;
import android.content.Intent;

import java.util.List;

import fm.doe.national.core.data.model.Survey;
import fm.doe.national.offline_sync.data.model.Device;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.subjects.Subject;

public interface OfflineAccessor {

    Subject<List<Device>> getDevicesSubject();

    void discoverDevices();

    void stopDiscoverDevices();

    Completable connect(Device device);

    Completable disconnect(Device device);

    Single<List<Survey>> requestSurveys(Device device);

    Single<Survey> requestFilledSurvey(Device device, long surveyId);

    void onBroadcastReceive(Context context, Intent intent);

//    void onConnectRecieved(Device device);
//
//    void onDisconnectRecieved(Device device);
//
//    void onSurveysRequestRecieved(Device device);
//
//    void onFilledSurveyRequestRecieved(Device device);

}
