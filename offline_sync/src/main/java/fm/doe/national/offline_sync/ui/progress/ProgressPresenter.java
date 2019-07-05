package fm.doe.national.offline_sync.ui.progress;

import android.util.Log;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.InjectViewState;

import fm.doe.national.cloud.di.CloudComponent;
import fm.doe.national.cloud.model.uploader.CloudUploader;
import fm.doe.national.core.data.model.ConflictResolveStrategy;
import fm.doe.national.core.ui.screens.base.BasePresenter;
import fm.doe.national.offline_sync.R;
import fm.doe.national.offline_sync.data.accessor.OfflineAccessor;
import fm.doe.national.offline_sync.data.model.SyncNotification;
import fm.doe.national.offline_sync.di.OfflineSyncComponent;
import fm.doe.national.offline_sync.domain.OfflineSyncUseCase;
import fm.doe.national.offline_sync.domain.SyncNotifier;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class ProgressPresenter extends BasePresenter<ProgressView> {

    private static final String TAG = ProgressPresenter.class.getName();

    private static final int PERCENTAGE_ALL = 100;
    private static final int PERCENTAGE_GET_SURVEY = 30;
    private static final int PERCENTAGE_PART_GET_PHOTOS = 30;
    private static final int PERCENTAGE_PUSH_SURVEY = 70;

    private final OfflineAccessor offlineAccessor;
    private final OfflineSyncUseCase useCase;
    private final SyncNotifier notifier;
    private final CloudUploader uploader;

    private int oneIncomePhotoPercentValue;
    private int currentProgress;

    public ProgressPresenter(OfflineSyncComponent component, CloudComponent cloudComponent) {
        this.offlineAccessor = component.getAccessor();
        this.useCase = component.getUseCase();
        this.notifier = component.getNotifier();
        this.uploader = cloudComponent.getCloudUploader();

        getViewState().setSurvey(useCase.getExternalSurvey());
        getViewState().setDescription(Text.from(R.string.hint_merge_in_progress));
        getViewState().setDevice(offlineAccessor.getCurrentConnectedDevice());

        addDisposable(
                offlineAccessor.requestFilledSurvey(useCase.getExternalSurvey().getId())
                        .flatMap(externalSurvey -> offlineAccessor.mergeSurveys(
                                useCase.getTargetSurvey(),
                                externalSurvey,
                                ConflictResolveStrategy.MINE)
                        )
                        .flatMapCompletable(offlineAccessor::pushSurvey)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            uploader.scheduleUploading(useCase.getTargetSurvey().getId());
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
            case DID_PUSH_SURVEY:
                setProgress(PERCENTAGE_PUSH_SURVEY);
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
