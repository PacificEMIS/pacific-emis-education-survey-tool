package fm.doe.national.report_core.ui.recommendations;

import com.omega_r.libs.omegatypes.Text;
import com.omegar.mvp.InjectViewState;

import fm.doe.national.core.ui.screens.base.BasePresenter;
import fm.doe.national.report_core.R;
import fm.doe.national.report_core.domain.ReportInteractor;
import fm.doe.national.report_core.model.recommendations.Recommendation;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class RecommendationsPresenter extends BasePresenter<RecommendationsView> {

    private ReportInteractor interactor;

    public RecommendationsPresenter(ReportInteractor interactor) {
        this.interactor = interactor;
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

    public void onRecommendationPressed(Recommendation recommendation) {
        // TODO: not implemented (planned in feature "new navigation")
        getViewState().showToast(Text.from(R.string.coming_soon));
    }

}
