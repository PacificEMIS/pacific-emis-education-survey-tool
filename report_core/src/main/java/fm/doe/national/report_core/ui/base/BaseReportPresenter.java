package fm.doe.national.report_core.ui.base;


import fm.doe.national.core.ui.screens.base.BasePresenter;
import fm.doe.national.report_core.domain.ReportInteractor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public abstract class BaseReportPresenter<T extends BaseReportView> extends BasePresenter<T> {

    public BaseReportPresenter(ReportInteractor interactor) {
        addDisposable(interactor.getHeaderItemSubject()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item -> {
                    if (!item.isEmpty()) {
                        getViewState().setHeaderItem(item);
                    }
                }, this::handleError));
    }

}
