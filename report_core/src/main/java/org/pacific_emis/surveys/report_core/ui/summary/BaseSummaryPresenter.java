package org.pacific_emis.surveys.report_core.ui.summary;

import org.pacific_emis.surveys.report_core.domain.ReportInteractor;
import org.pacific_emis.surveys.report_core.ui.base.BaseReportPresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public abstract class BaseSummaryPresenter<T extends BaseSummaryView> extends BaseReportPresenter<T> {

    public BaseSummaryPresenter(ReportInteractor interactor) {
        super(interactor);
        addDisposable(interactor.getSummarySubjectObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().setLoadingVisibility(true))
                .doFinally(() -> getViewState().setLoadingVisibility(false))
                .subscribe(getViewState()::setSummaryData, this::handleError));
    }

}
