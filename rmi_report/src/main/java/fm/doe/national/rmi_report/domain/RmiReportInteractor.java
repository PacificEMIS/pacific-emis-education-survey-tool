package fm.doe.national.rmi_report.domain;

import fm.doe.national.report_core.domain.ReportInteractor;
import fm.doe.national.rmi_report.model.SchoolAccreditationTallyLevel;
import io.reactivex.subjects.Subject;

public interface RmiReportInteractor extends ReportInteractor {
    Subject<SchoolAccreditationTallyLevel> getLevelSubject();
}
