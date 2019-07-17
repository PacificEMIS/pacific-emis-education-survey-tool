package fm.doe.national.remote_storage.data.model;

import java.util.List;

import fm.doe.national.report_core.model.SummaryViewData;
import fm.doe.national.report_core.model.recommendations.Recommendation;
import fm.doe.national.report_core.ui.level_legend.LevelLegendView;

public class ReportWrapper {

    private LevelLegendView.Item header;
    private List<SummaryViewData> summary;
    private List<Recommendation> recommendations;

    public ReportWrapper(LevelLegendView.Item header, List<SummaryViewData> summary, List<Recommendation> recommendations) {
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

    public List<Recommendation> getRecommendations() {
        return recommendations;
    }
}
