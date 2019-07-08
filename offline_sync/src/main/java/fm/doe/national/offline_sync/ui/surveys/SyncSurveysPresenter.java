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
    private Survey selectedSurvey;

    public SyncSurveysPresenter(OfflineSyncComponent component) {
        getViewState().setNextEnabled(false);
        offlineAccessor = component.getAccessor();
        useCase = component.getUseCase();
        targetSurvey = useCase.getTargetSurvey();
        onRefresh();
    }

    public void onSurveyPressed(Survey survey) {
        selectedSurvey = survey;
        getViewState().setNextEnabled(true);
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

    public void onNextPressed() {
        if (selectedSurvey == null) {
            return;
        }

        useCase.setExternalSurvey(selectedSurvey);
        getViewState().navigateToProgress();
        getViewState().close();
    }
}
