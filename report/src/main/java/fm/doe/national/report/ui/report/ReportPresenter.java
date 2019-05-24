package fm.doe.national.report.ui.report;

import com.omegar.mvp.InjectViewState;

import javax.inject.Inject;

import fm.doe.national.core.interactors.SurveyInteractor;
import fm.doe.national.core.ui.screens.base.BasePresenter;
import fm.doe.national.report.di.ReportComponent;
import fm.doe.national.report_core.domain.ReportsProvider;

@InjectViewState
public class ReportPresenter extends BasePresenter<ReportView> {

    @Inject
    ReportsProvider reportsProvider;

    @Inject
    SurveyInteractor surveyInteractor;

    public ReportPresenter(ReportComponent component) {
        reportsProvider = component.getReportsProvider();
        surveyInteractor = component.getSurveyInteractor();
        reportsProvider.requestReports(surveyInteractor.getCurrentSurvey());
        getViewState().setReportPages(reportsProvider.getPages());
    }
}
