package fm.doe.national.core.data.model.recommendations;

import fm.doe.national.core.data.model.Criteria;

public class CriteriaRecommendation extends Recommendation<Criteria> {

    private static final int LEVEL = 2;

    public CriteriaRecommendation(Criteria object) {
        super(object, object.getSuffix() + " " + object.getTitle(), LEVEL);
    }
}
