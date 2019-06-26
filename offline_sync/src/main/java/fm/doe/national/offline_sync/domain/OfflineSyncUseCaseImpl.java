package fm.doe.national.offline_sync.domain;

import android.app.Activity;

import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.utils.LifecycleListener;
import fm.doe.national.offline_sync.data.accessor.OfflineAccessor;
import fm.doe.national.offline_sync.ui.devices.PairedDevicesActivity;
import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.CompletableSubject;

public class OfflineSyncUseCaseImpl implements OfflineSyncUseCase {

    private final LifecycleListener lifecycleListener;
    private final OfflineAccessor offlineAccessor;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private CompletableSubject completableSubject;
    private Survey targetSurvey;

    public OfflineSyncUseCaseImpl(LifecycleListener lifecycleListener, OfflineAccessor offlineAccessor) {
        this.lifecycleListener = lifecycleListener;
        this.offlineAccessor = offlineAccessor;
    }

    @Override
    public Completable execute(Survey survey) {
        targetSurvey = survey;
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
    public Survey getTargetSurvey() {
        return targetSurvey;
    }

    @Override
    public void finish() {
        offlineAccessor.disconnect();
        offlineAccessor.stopDiscoverDevices();
        offlineAccessor.becomeUnavailableToConnect();
        compositeDisposable.dispose();
    }

}
