package fm.doe.national.remote_storage.data.model;

import java.util.List;

import fm.doe.national.fcm_report.data.model.SchoolAccreditationLevel;
import fm.doe.national.report_core.model.SummaryViewData;
import fm.doe.national.report_core.model.recommendations.FlattenRecommendationsWrapper;
import fm.doe.national.report_core.ui.level_legend.LevelLegendView;
import fm.doe.national.rmi_report.model.SchoolAccreditationTallyLevel;

public class ReportWrapper {

    private LevelLegendView.Item header;
    private List<SummaryViewData> summary;
    private FlattenRecommendationsWrapper recommendations;
    private SchoolAccreditationLevel schoolAccreditationLevel;
    private SchoolAccreditationTallyLevel schoolAccreditationTallyLevel;

    public ReportWrapper(LevelLegendView.Item header, List<SummaryViewData> summary, FlattenRecommendationsWrapper recommendations) {
        this.header = header;
        this.summary = summary;
        this.recommendations = recommendations;
    }

    public LevelLegendView.Item getHeader() {
        return header;
    }

    public List<SummaryViewData> getSummary() {
        return summary;
    }

    public FlattenRecommendationsWrapper getRecommendations() {
        return recommendations;
    }

    public SchoolAccreditationLevel getSchoolAccreditationLevel() {
        return schoolAccreditationLevel;
    }

    public SchoolAccreditationTallyLevel getSchoolAccreditationTallyLevel() {
        return schoolAccreditationTallyLevel;
    }

    public ReportWrapper setSchoolAccreditationLevel(SchoolAccreditationLevel schoolAccreditationLevel) {
        this.schoolAccreditationLevel = schoolAccreditationLevel;
        return this;
    }

    public ReportWrapper setSchoolAccreditationTallyLevel(SchoolAccreditationTallyLevel schoolAccreditationTallyLevel) {
        this.schoolAccreditationTallyLevel = schoolAccreditationTallyLevel;
        return this;
    }
}
