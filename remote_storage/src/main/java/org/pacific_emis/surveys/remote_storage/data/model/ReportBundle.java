package org.pacific_emis.surveys.remote_storage.data.model;

import java.util.List;

import org.pacific_emis.surveys.fsm_report.model.SchoolAccreditationLevel;
import org.pacific_emis.surveys.report_core.model.SummaryViewData;
import org.pacific_emis.surveys.report_core.model.recommendations.FlattenRecommendationsWrapper;
import org.pacific_emis.surveys.report_core.ui.level_legend.LevelLegendView;
import org.pacific_emis.surveys.rmi_report.model.SchoolAccreditationTallyLevel;

public class ReportBundle {

    private LevelLegendView.Item header;
    private List<SummaryViewData> summary;
    private FlattenRecommendationsWrapper recommendations;
    private SchoolAccreditationLevel schoolAccreditationLevel;
    private SchoolAccreditationTallyLevel schoolAccreditationTallyLevel;

    public ReportBundle(LevelLegendView.Item header, List<SummaryViewData> summary, FlattenRecommendationsWrapper recommendations) {
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

    public ReportBundle setSchoolAccreditationLevel(SchoolAccreditationLevel schoolAccreditationLevel) {
        this.schoolAccreditationLevel = schoolAccreditationLevel;
        return this;
    }

    public ReportBundle setSchoolAccreditationTallyLevel(SchoolAccreditationTallyLevel schoolAccreditationTallyLevel) {
        this.schoolAccreditationTallyLevel = schoolAccreditationTallyLevel;
        return this;
    }
}
