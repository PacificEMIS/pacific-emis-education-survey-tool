package fm.doe.national.report_core.model.recommendations;

import fm.doe.national.core.data.model.Standard;

public class StandardRecommendation extends Recommendation<Standard> {

    private static final int LEVEL = 1;

    public StandardRecommendation(Standard object) {
        super(object, object.getSuffix() + " " + object.getTitle(), LEVEL);
    }

}