package fm.doe.national.report_core.model.recommendations;

import com.omega_r.libs.omegatypes.Text;

import fm.doe.national.core.data.model.Standard;
import fm.doe.national.report_core.R;

public class StandardRecommendation extends Recommendation<Standard> {

    public StandardRecommendation(Standard object) {
        super(object, Text.from(R.string.format_recommendation_standard, object.getSuffix(), object.getTitle()));
    }

}
