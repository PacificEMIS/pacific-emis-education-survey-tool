package fm.doe.national.report_core.ui.summary;

import com.omegar.mvp.InjectViewState;

import fm.doe.national.report_core.domain.ReportInteractor;
import fm.doe.national.report_core.ui.base.BaseReportPresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class SummaryPresenter extends BaseReportPresenter<SummaryView> {

    private final ReportInteractor interactor;

    public SummaryPresenter(ReportInteractor interactor) {
        super(interactor);
        this.interactor = interactor;
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
