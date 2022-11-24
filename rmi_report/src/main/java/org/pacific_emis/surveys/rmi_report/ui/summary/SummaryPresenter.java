package org.pacific_emis.surveys.rmi_report.ui.summary;

import com.omegar.mvp.InjectViewState;

import org.pacific_emis.surveys.report_core.domain.ReportInteractor;
import org.pacific_emis.surveys.report_core.ui.summary.BaseSummaryPresenter;

@InjectViewState
public class SummaryPresenter extends BaseSummaryPresenter<SummaryView> {

    public SummaryPresenter(ReportInteractor interactor) {
        super(interactor);
    }

}
