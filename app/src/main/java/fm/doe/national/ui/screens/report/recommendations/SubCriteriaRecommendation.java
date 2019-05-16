package fm.doe.national.ui.screens.report.recommendations;

import fm.doe.national.data.model.SubCriteria;

public class SubCriteriaRecommendation extends Recommendation<SubCriteria> {

    public SubCriteriaRecommendation(SubCriteria object) {
        super(object, object.getSuffix() + " " + object.getTitle(), 3);
    }
}
