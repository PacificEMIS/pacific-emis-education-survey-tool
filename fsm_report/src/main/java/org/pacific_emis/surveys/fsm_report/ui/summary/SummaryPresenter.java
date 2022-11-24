package org.pacific_emis.surveys.fsm_report.ui.summary;

import com.omegar.mvp.InjectViewState;

import org.pacific_emis.surveys.report_core.domain.ReportInteractor;
import org.pacific_emis.surveys.report_core.ui.base.BaseReportPresenter;
import org.pacific_emis.surveys.report_core.ui.summary.BaseSummaryPresenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class SummaryPresenter extends BaseSummaryPresenter<SummaryView> {

    public SummaryPresenter(ReportInteractor interactor) {
        super(interactor);
    }

}
