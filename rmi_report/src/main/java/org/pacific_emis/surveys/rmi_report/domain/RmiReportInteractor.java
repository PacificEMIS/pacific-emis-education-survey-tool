package org.pacific_emis.surveys.rmi_report.domain;

import org.pacific_emis.surveys.report_core.domain.ReportInteractor;
import org.pacific_emis.surveys.rmi_report.model.SchoolAccreditationTallyLevel;
import io.reactivex.Observable;

public interface RmiReportInteractor extends ReportInteractor {
    Observable<SchoolAccreditationTallyLevel> getLevelObservable();
}
