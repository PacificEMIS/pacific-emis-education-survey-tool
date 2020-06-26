package org.pacific_emis.surveys.offline_sync.data.accessor;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

import java.util.List;

import org.pacific_emis.surveys.core.data.model.ConflictResolveStrategy;
import org.pacific_emis.surveys.core.data.model.Survey;
import org.pacific_emis.surveys.offline_sync.data.bluetooth_threads.ConnectionState;
import org.pacific_emis.surveys.offline_sync.data.model.Device;
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

    Single<List<Survey>> requestSurveys(String schoolId, String surveyTag);

    Single<Survey> requestFilledSurvey(long surveyId);

    void onBroadcastReceive(Context context, Intent intent);

    Single<Survey> mergeSurveys(Survey targetSurvey, Survey externalSurvey, ConflictResolveStrategy strategy);

    void setSyncUseCase(SyncUseCase syncUseCase);

    @Nullable
    Device getCurrentConnectedDevice();

    interface SyncUseCase {

        Survey getTargetSurvey();

        void setTargetSurvey(Survey survey);

    }
}
