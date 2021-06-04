package org.pacific_emis.surveys.ui.dialogs.merge_progress;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.InjectViewState;

import org.pacific_emis.surveys.R;
import org.pacific_emis.surveys.app_support.MicronesiaApplication;
import org.pacific_emis.surveys.core.data.model.Survey;
import org.pacific_emis.surveys.core.ui.screens.base.BasePresenter;
import org.pacific_emis.surveys.offline_sync.data.accessor.OfflineAccessor;
import org.pacific_emis.surveys.offline_sync.data.bluetooth_threads.ConnectionState;
import org.pacific_emis.surveys.offline_sync.data.model.SyncNotification;
import org.pacific_emis.surveys.offline_sync.domain.OfflineSyncUseCase;
import org.pacific_emis.surveys.offline_sync.domain.SyncNotifier;
import org.pacific_emis.surveys.remote_storage.data.accessor.RemoteStorageAccessor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class MergeProgressPresenter extends BasePresenter<MergeProgressView> {

    private static final int PERCENTAGE_ALL = 100;
    private static final int PERCENTAGE_SEND_AVAILABLE_SURVEYS = 10;
    private static final int PERCENTAGE_SEND_SURVEY = 50;
    private static final int PERCENTAGE_PART_SEND_ALL_PHOTOS = 50;

    private final OfflineAccessor offlineAccessor = MicronesiaApplication.getInjection().getOfflineSyncComponent().getAccessor();
    private final OfflineSyncUseCase useCase = MicronesiaApplication.getInjection().getOfflineSyncComponent().getUseCase();
    private final SyncNotifier notifier = MicronesiaApplication.getInjection().getOfflineSyncComponent().getNotifier();
    private final RemoteStorageAccessor remoteStorageAccessor = MicronesiaApplication.getInjection()
            .getRemoteStorageComponent()
            .getRemoteStorageAccessor();

    private int oneOutcomePhotoPercentValue;
    private int currentProgress;

    public MergeProgressPresenter() {
        addDisposable(
                offlineAccessor.getConnectionStateObservable()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::onConnectionStateChanged, this::handleError)
        );
        addDisposable(
                notifier.getNotificationsObservable()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::handleNotification, this::handleError)
        );
        getViewState().setDescription(Text.from(R.string.hint_merge_waiting));
    }

    public void onEndSessionPressed() {
        useCase.finish();
        getViewState().close();
    }

    private void onConnectionStateChanged(ConnectionState state) {
        switch (state) {
            case CONNECTED:
                getViewState().setDescription(Text.from(R.string.hint_merge_in_progress));
                break;
        }
    }

    private void handleNotification(SyncNotification notification) {
        switch (notification.getType()) {
            case DID_SEND_AVAILABLE_SURVEYS:
                setProgress(PERCENTAGE_SEND_AVAILABLE_SURVEYS);
                break;
            case DID_SEND_SURVEY:
                setProgress(PERCENTAGE_SEND_SURVEY);
                break;
            case WILL_SEND_PHOTOS:
                oneOutcomePhotoPercentValue = notification.getValue() == 0 ?
                        PERCENTAGE_PART_SEND_ALL_PHOTOS :
                        (PERCENTAGE_PART_SEND_ALL_PHOTOS / notification.getValue());
                break;
            case DID_SEND_PHOTO:
                addProgress(oneOutcomePhotoPercentValue);
                break;
            case DID_FINISH_SYNC:
                setProgress(PERCENTAGE_ALL);
                getViewState().setDescription(Text.from(R.string.hint_merge_successful));
                Survey survey = useCase.getTargetSurvey();

                if (survey != null) {
                    remoteStorageAccessor.scheduleUploading(survey.getId());
                }

                break;
        }
    }

    private void setProgress(int progress) {
        currentProgress = progress;
        getViewState().setProgress(currentProgress);
    }

    private void addProgress(int progress) {
        currentProgress += progress;
        getViewState().setProgress(currentProgress);
    }

}
