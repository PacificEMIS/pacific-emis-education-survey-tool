package org.pacific_emis.surveys.fsm_report.domain;

import org.pacific_emis.surveys.fsm_report.model.SchoolAccreditationLevel;
import org.pacific_emis.surveys.report_core.domain.ReportInteractor;
import io.reactivex.Observable;

public interface FsmReportInteractor extends ReportInteractor {

    Observable<SchoolAccreditationLevel> getLevelObservable();

}
