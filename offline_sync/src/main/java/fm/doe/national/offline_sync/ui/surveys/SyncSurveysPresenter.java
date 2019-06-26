package fm.doe.national.offline_sync.ui.surveys;

import com.omegar.mvp.InjectViewState;

import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.ui.screens.base.BasePresenter;
import fm.doe.national.offline_sync.data.accessor.OfflineAccessor;
import fm.doe.national.offline_sync.di.OfflineSyncComponent;
import fm.doe.national.offline_sync.domain.OfflineSyncUseCase;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class SyncSurveysPresenter extends BasePresenter<SyncSurveysView> {

    private final OfflineAccessor offlineAccessor;
    private final OfflineSyncUseCase useCase;
    private Survey targetSurvey;

    public SyncSurveysPresenter(OfflineSyncComponent component) {
        offlineAccessor = component.getAccessor();
        useCase = component.getUseCase();
        targetSurvey = useCase.getTargetSurvey();
        onRefresh();
    }

    public void onSurveyPressed(Survey survey) {
        addDisposable(
                offlineAccessor.requestFilledSurvey(survey.getId())
                        .flatMapCompletable(externalSurvey -> offlineAccessor.mergeSurveys(targetSurvey, externalSurvey))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(d -> getViewState().showWaiting())
                        .doFinally(getViewState()::hideWaiting)
                        .subscribe(getViewState()::close, this::handleError)
        );
    }

    public void onRefresh() {
        addDisposable(
                offlineAccessor.requestSurveys(targetSurvey.getSchoolId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(d -> getViewState().setListLoadingVisible(true))
                        .doFinally(() -> getViewState().setListLoadingVisible(false))
                        .subscribe(getViewState()::setSurveys, this::handleError)
        );
    }
}
