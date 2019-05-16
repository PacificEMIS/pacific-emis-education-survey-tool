package fm.doe.national.ui.screens.report;

import com.omegar.mvp.InjectViewState;

import fm.doe.national.app_support.MicronesiaApplication;
import fm.doe.national.domain.SurveyInteractor;
import fm.doe.national.ui.screens.base.BasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class ReportPresenter extends BasePresenter<ReportView> {

    private final SurveyInteractor interactor = MicronesiaApplication.getAppComponent().getSurveyInteractor();

    ReportPresenter() {
        loadSummary();
        loadRecommendations();
    }

    private void loadSummary() {
        addDisposable(interactor.requestSummary()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().setSummaryLoadingVisibility(true))
                .doFinally(() -> getViewState().setSummaryLoadingVisibility(false))
                .subscribe(getViewState()::setSummaryData, this::handleError));
    }

    private void loadRecommendations() {
        addDisposable(interactor.requestRecommendations()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().setRecommendationsLoadingVisibility(true))
                .doFinally(() -> getViewState().setRecommendationsLoadingVisibility(false))
                .subscribe(getViewState()::setRecommendations, this::handleError));
    }

}
