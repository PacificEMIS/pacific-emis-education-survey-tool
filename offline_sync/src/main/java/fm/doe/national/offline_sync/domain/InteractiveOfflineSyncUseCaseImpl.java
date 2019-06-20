package fm.doe.national.offline_sync.domain;

import android.app.Activity;

import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.utils.LifecycleListener;
import fm.doe.national.offline_sync.data.model.Device;
import fm.doe.national.offline_sync.ui.devices.PairedDevicesActivity;
import io.reactivex.Completable;
import io.reactivex.subjects.CompletableSubject;

public class InteractiveOfflineSyncUseCaseImpl implements InteractiveOfflineSyncUseCase {

    private final LifecycleListener lifecycleListener;

    private CompletableSubject completableSubject;
    private Survey parentSurvey;

    public InteractiveOfflineSyncUseCaseImpl(LifecycleListener lifecycleListener) {
        this.lifecycleListener = lifecycleListener;
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

    @Override
    public void onDeviceSelected(Device device) {

    }

    @Override
    public void onSurveySelected(Survey survey) {

    }
}
