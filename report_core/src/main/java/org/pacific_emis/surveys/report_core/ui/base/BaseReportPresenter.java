package org.pacific_emis.surveys.report_core.ui.base;


import org.pacific_emis.surveys.core.ui.screens.base.BasePresenter;
import org.pacific_emis.surveys.report_core.domain.ReportInteractor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public abstract class BaseReportPresenter<T extends BaseReportView> extends BasePresenter<T> {

    public BaseReportPresenter(ReportInteractor interactor) {
        addDisposable(interactor.getHeaderItemObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item -> {
                    if (!item.isEmpty()) {
                        getViewState().setHeaderItem(item);
                    }
                }, this::handleError));
    }

}
