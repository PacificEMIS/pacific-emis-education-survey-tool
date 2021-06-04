package org.pacific_emis.surveys.offline_sync.ui.progress;

import android.util.Log;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.InjectViewState;

import org.pacific_emis.surveys.core.data.model.ConflictResolveStrategy;
import org.pacific_emis.surveys.core.ui.screens.base.BasePresenter;
import org.pacific_emis.surveys.offline_sync.R;
import org.pacific_emis.surveys.offline_sync.data.accessor.OfflineAccessor;
import org.pacific_emis.surveys.offline_sync.data.model.SyncNotification;
import org.pacific_emis.surveys.offline_sync.di.OfflineSyncComponent;
import org.pacific_emis.surveys.offline_sync.domain.OfflineSyncUseCase;
import org.pacific_emis.surveys.offline_sync.domain.SyncNotifier;
import org.pacific_emis.surveys.remote_storage.data.accessor.RemoteStorageAccessor;
import org.pacific_emis.surveys.remote_storage.di.RemoteStorageComponent;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class ProgressPresenter extends BasePresenter<ProgressView> {

    private static final String TAG = ProgressPresenter.class.getName();

    private static final int PERCENTAGE_ALL = 100;
    private static final int PERCENTAGE_GET_SURVEY = 50;
    private static final int PERCENTAGE_PART_GET_PHOTOS = 50;

    private final OfflineAccessor offlineAccessor;
    private final OfflineSyncUseCase useCase;
    private final SyncNotifier notifier;
    private final RemoteStorageAccessor remoteStorageAccessor;

    private int oneIncomePhotoPercentValue;
    private int currentProgress;

    public ProgressPresenter(OfflineSyncComponent component, RemoteStorageComponent remoteStorageComponent) {
        this.offlineAccessor = component.getAccessor();
        this.useCase = component.getUseCase();
        this.notifier = component.getNotifier();
        this.remoteStorageAccessor = remoteStorageComponent.getRemoteStorageAccessor();

        getViewState().setSurvey(useCase.getExternalSurvey());
        getViewState().setDescription(Text.from(R.string.hint_merge_in_progress));
        getViewState().setDevice(offlineAccessor.getCurrentConnectedDevice());

        addDisposable(
                offlineAccessor.requestFilledSurvey(useCase.getExternalSurvey().getId())
                        .flatMap(externalSurvey -> offlineAccessor.mergeSurveys(
                                useCase.getTargetSurvey(),
                                externalSurvey,
                                ConflictResolveStrategy.THEIRS)
                        )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(survey -> {
                            remoteStorageAccessor.scheduleUploading(useCase.getTargetSurvey().getId());
                            useCase.finish();
                        }, this::handleError)
        );

        addDisposable(
                notifier.getNotificationsObservable()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::handleNotification, this::handleError)
        );
    }

    public void onEndSessionPressed() {
        useCase.finish();
        getViewState().close();
    }

    private void handleNotification(SyncNotification notification) {
        Log.d(TAG, "handleNotification(" + notification.getType() + ")");
        switch (notification.getType()) {
            case WILL_SAVE_PHOTOS:
                setProgress(PERCENTAGE_GET_SURVEY);
                oneIncomePhotoPercentValue = notification.getValue() == 0 ?
                        PERCENTAGE_PART_GET_PHOTOS :
                        (PERCENTAGE_PART_GET_PHOTOS / notification.getValue());
                break;
            case DID_SAVE_PHOTO:
                addProgress(oneIncomePhotoPercentValue);
                break;
            case DID_FINISH_SYNC:
                setProgress(PERCENTAGE_ALL);
                getViewState().setDescription(Text.from(R.string.hint_merge_successful));
                break;
        }
    }

    private void setProgress(int progress) {
        currentProgress = progress;
        getViewState().setMergeProgress(currentProgress);

        if (progress >= PERCENTAGE_ALL) {
            getViewState().showContinue();
        }
    }

    private void addProgress(int progress) {
        currentProgress += progress;
        getViewState().setMergeProgress(currentProgress);
    }
}
