package fm.doe.national.fsm_report.domain;

import fm.doe.national.fsm_report.model.SchoolAccreditationLevel;
import fm.doe.national.report_core.domain.ReportInteractor;
import io.reactivex.Observable;

public interface FsmReportInteractor extends ReportInteractor {

    Observable<SchoolAccreditationLevel> getLevelObservable();

}
