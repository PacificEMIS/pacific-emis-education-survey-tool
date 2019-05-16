package fm.doe.national.ui.screens.report;

import com.omegar.mvp.InjectViewState;

import fm.doe.national.app_support.MicronesiaApplication;
import fm.doe.national.domain.SurveyInteractor;
import fm.doe.national.ui.screens.base.BasePresenter;

@InjectViewState
public class ReportPresenter extends BasePresenter<ReportView> {

    private final SurveyInteractor interactor = MicronesiaApplication.getAppComponent().getSurveyInteractor();

    ReportPresenter() {
    }

}
