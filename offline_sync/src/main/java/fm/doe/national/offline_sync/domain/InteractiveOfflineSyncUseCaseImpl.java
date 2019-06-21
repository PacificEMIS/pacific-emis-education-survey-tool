package fm.doe.national.offline_sync.domain;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;

import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.utils.LifecycleListener;
import fm.doe.national.offline_sync.data.accessor.OfflineAccessor;
import fm.doe.national.offline_sync.data.model.Device;
import fm.doe.national.offline_sync.ui.devices.PairedDevicesActivity;
import io.reactivex.Completable;
import io.reactivex.subjects.CompletableSubject;

public class InteractiveOfflineSyncUseCaseImpl implements InteractiveOfflineSyncUseCase {

    private final LifecycleListener lifecycleListener;
    private final OfflineAccessor offlineAccessor;

    private CompletableSubject completableSubject;
    private Survey parentSurvey;

    public InteractiveOfflineSyncUseCaseImpl(LifecycleListener lifecycleListener, OfflineAccessor offlineAccessor) {
        this.lifecycleListener = lifecycleListener;
        this.offlineAccessor = offlineAccessor;
    }

    @Override
    public Completable execute(Survey survey) {
        parentSurvey = survey;
        completableSubject = CompletableSubject.create();
        selectDevice();
        return completableSubject;
    }

    private void selectDevice() {
        Activity activity = lifecycleListener.getCurrentActivity();

        if (activity == null) {
            completableSubject.onError(new IllegalStateException());
            return;
        }

        activity.startActivity(PairedDevicesActivity.createIntent(activity));
    }

    @SuppressLint("CheckResult")
    @Override
    public void onDeviceSelected(Device device) {
        offlineAccessor.connect(device)
                .andThen(offlineAccessor.requestSurveys())
                .subscribe(surveys -> Log.d("LOG", surveys.toString()), Throwable::printStackTrace);
    }

    @Override
    public void onSurveySelected(Survey survey) {

    }
}
