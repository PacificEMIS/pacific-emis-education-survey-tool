package fm.doe.national.core.ui.screens.report.base;

import fm.doe.national.core.interactors.ReportInteractor;
import fm.doe.national.core.ui.screens.base.BasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BaseReportPresenter<T extends BaseReportView> extends BasePresenter<T> {

    protected final ReportInteractor interactor = MicronesiaApplication.getAppComponent().getReportInteractor();

    public BaseReportPresenter() {
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
