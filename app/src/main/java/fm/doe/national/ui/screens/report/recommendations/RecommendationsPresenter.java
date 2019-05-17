package fm.doe.national.ui.screens.report.recommendations;

import com.omegar.mvp.InjectViewState;

import fm.doe.national.app_support.MicronesiaApplication;
import fm.doe.national.domain.ReportInteractor;
import fm.doe.national.ui.screens.base.BasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class RecommendationsPresenter extends BasePresenter<RecommendationsView> {

    private final ReportInteractor interactor = MicronesiaApplication.getAppComponent().getReportInteractor();

    public RecommendationsPresenter() {
        loadRecommendations();
    }

    private void loadRecommendations() {
        addDisposable(interactor.getRecommendationsSubject()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().setRecommendationsLoadingVisibility(true))
                .doFinally(() -> getViewState().setRecommendationsLoadingVisibility(false))
                .subscribe(getViewState()::setRecommendations, this::handleError));
    }

}
