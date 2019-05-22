package fm.doe.national.fcm_report.domain;

import fm.doe.national.core.interactors.ReportInteractor;
import fm.doe.national.core.ui.summary_header.SummaryHeaderView;
import fm.doe.national.fcm_report.data.model.SchoolAccreditationLevel;
import io.reactivex.subjects.Subject;

public interface FcmReportInteractor extends ReportInteractor {

    Subject<SchoolAccreditationLevel> getLevelSubject();

    Subject<SummaryHeaderView.Item> getHeaderItemSubject();

}
