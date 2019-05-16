package fm.doe.national.ui.screens.report.recommendations;

import fm.doe.national.data.model.Standard;

public class StandardRecommendation extends Recommendation<Standard> {

    public StandardRecommendation(Standard object) {
        super(object, object.getSuffix() + " " + object.getTitle(), 1);
    }

}
