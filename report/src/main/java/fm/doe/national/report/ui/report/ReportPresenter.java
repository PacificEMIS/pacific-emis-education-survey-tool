package fm.doe.national.report.ui.report;

import com.omegar.mvp.InjectViewState;

import fm.doe.national.core.interactors.SurveyInteractor;
import fm.doe.national.core.ui.screens.base.BasePresenter;
import fm.doe.national.report.di.ReportComponent;
import fm.doe.national.report_core.domain.ReportInteractor;
import fm.doe.national.report_core.domain.ReportsProvider;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class ReportPresenter extends BasePresenter<ReportView> {

    private final ReportsProvider reportsProvider;
    private final SurveyInteractor surveyInteractor;
    private final ReportInteractor reportInteractor;

    public ReportPresenter(ReportComponent component) {
        reportsProvider = component.getReportsProvider();
        surveyInteractor = component.getSurveyInteractor();
        reportInteractor = component.getReportInteractor();
        reportsProvider.requestReports(surveyInteractor.getCurrentSurvey());
        getViewState().setReportPages(reportsProvider.getPages());

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
