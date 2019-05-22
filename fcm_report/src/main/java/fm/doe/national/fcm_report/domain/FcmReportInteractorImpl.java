package fm.doe.national.fcm_report.domain;

import fm.doe.national.core.data.model.Level;
import fm.doe.national.core.interactors.BaseReportInteractor;

public class FcmReportInteractorImpl extends BaseReportInteractor implements FcmReportInteractor {

    @Override
    protected Level createLevel(int completed, int total) {
        return null;
    }

    @Override
    public Subject<SchoolAccreditationLevel> getLevelSubject() {
        return null;
    }

    @Override
    public Subject<SummaryHeaderView.Item> getHeaderItemSubject() {
        return null;
    }

}
