package org.pacific_emis.surveys.report_core.model.recommendations;

import java.util.List;

import org.pacific_emis.surveys.accreditation_core.data.model.AccreditationSurvey;

public class FlattenRecommendationsWrapper {

    private final List<Recommendation> recommendations;
    private final AccreditationSurvey flattenSurvey;

    public FlattenRecommendationsWrapper(List<Recommendation> recommendations, AccreditationSurvey flattenSurvey) {
        this.recommendations = recommendations;
        this.flattenSurvey = flattenSurvey;
    }

    public List<Recommendation> getRecommendations() {
        return recommendations;
    }

    public AccreditationSurvey getFlattenSurvey() {
        return flattenSurvey;
    }
}
