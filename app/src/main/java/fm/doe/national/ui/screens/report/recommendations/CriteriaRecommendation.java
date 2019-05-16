package fm.doe.national.ui.screens.report.recommendations;

import fm.doe.national.data.model.Criteria;

public class CriteriaRecommendation extends Recommendation<Criteria> {

    public CriteriaRecommendation(Criteria object) {
        super(object, object.getSuffix() + " " + object.getTitle(), 2);
    }
}
