package fm.doe.national.rmi_report.domain;

import fm.doe.national.report_core.domain.ReportInteractor;
import fm.doe.national.rmi_report.model.SchoolAccreditationTallyLevel;
import io.reactivex.Observable;

public interface RmiReportInteractor extends ReportInteractor {
    Observable<SchoolAccreditationTallyLevel> getLevelObservable();
}
