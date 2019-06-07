package fm.doe.national.report.ui.report;

import com.omegar.mvp.InjectViewState;

import fm.doe.national.core.ui.screens.base.BasePresenter;
import fm.doe.national.report.di.ReportComponent;
import fm.doe.national.report_core.domain.ReportInteractor;
import fm.doe.national.report_core.domain.ReportsProvider;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class ReportPresenter extends BasePresenter<ReportView> {

    private final ReportsProvider reportsProvider;
    private final ReportInteractor reportInteractor;

    public ReportPresenter(ReportComponent component) {
        reportsProvider = component.getReportsProvider();
        reportInteractor = component.getReportInteractor();
        getViewState().setReportPages(reportsProvider.getPages());
        reportsProvider.requestReports();
        addDisposable(reportInteractor.getHeaderItemSubject()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item -> {
                    if (!item.isEmpty()) {
                        getViewState().setHeaderItem(item);
                    }
                }, this::handleError));
    }
}
