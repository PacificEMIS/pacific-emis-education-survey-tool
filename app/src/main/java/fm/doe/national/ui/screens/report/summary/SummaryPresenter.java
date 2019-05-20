package fm.doe.national.ui.screens.report.summary;

import com.omegar.mvp.InjectViewState;

import fm.doe.national.ui.screens.report.base.BaseReportPresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class SummaryPresenter extends BaseReportPresenter<SummaryView> {

    public SummaryPresenter() {
        super();
        loadSummary();
    }

    private void loadSummary() {
        addDisposable(interactor.getSummarySubject()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().setLoadingVisibility(true))
                .doFinally(() -> getViewState().setLoadingVisibility(false))
                .subscribe(getViewState()::setSummaryData, this::handleError));
    }
}
