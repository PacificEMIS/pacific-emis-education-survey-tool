package fm.doe.national.ui.screens.report;

import com.omegar.mvp.InjectViewState;

import java.util.Arrays;

import fm.doe.national.app_support.MicronesiaApplication;
import fm.doe.national.data.model.Survey;
import fm.doe.national.domain.ReportInteractor;
import fm.doe.national.domain.SurveyInteractor;
import fm.doe.national.ui.screens.base.BasePresenter;

@InjectViewState
public class ReportPresenter extends BasePresenter<ReportView> {

    private static final String NO_NAME = "No Name";

    private final SurveyInteractor surveyInteractor = MicronesiaApplication.getAppComponent().getSurveyInteractor();
    private final ReportInteractor reportInteractor = MicronesiaApplication.getAppComponent().getReportInteractor();

    public ReportPresenter() {
        Survey survey = surveyInteractor.getCurrentSurvey();
        reportInteractor.requestReports(survey);

        ReportView view = getViewState();
        view.setSchoolId(survey.getSchoolId());
        view.setSchoolName(survey.getSchoolName());
        view.setDateOfAccreditation(survey.getDate());
        view.setPrincipalName(null); // TODO: principal name in WASH surveys? (not implemented)
        view.setLegend(Arrays.asList(ReportLevel.values()));
    }
}
