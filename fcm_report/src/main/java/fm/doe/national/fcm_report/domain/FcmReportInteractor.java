package fm.doe.national.fcm_report.domain;

import fm.doe.national.fcm_report.data.model.SchoolAccreditationLevel;
import fm.doe.national.report_core.domain.ReportInteractor;
import io.reactivex.Observable;

public interface FcmReportInteractor extends ReportInteractor {

    Observable<SchoolAccreditationLevel> getLevelObservable();

}
