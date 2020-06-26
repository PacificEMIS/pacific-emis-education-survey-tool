package org.pacific_emis.surveys.report.ui.report;

import com.omegar.mvp.InjectViewState;

import org.pacific_emis.surveys.core.ui.screens.base.BasePresenter;
import org.pacific_emis.surveys.report.di.ReportComponent;
import org.pacific_emis.surveys.report_core.domain.ReportInteractor;
import org.pacific_emis.surveys.report_core.domain.ReportsProvider;
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
        addDisposable(reportInteractor.getHeaderItemObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item -> {
                    if (!item.isEmpty()) {
                        getViewState().setHeaderItem(item);
                    }
                }, this::handleError));
    }
}
