package org.pacific_emis.surveys.report_core.model.recommendations;

import com.omega_r.libs.omegatypes.Text;

import org.pacific_emis.surveys.accreditation_core.data.model.Standard;
import org.pacific_emis.surveys.report_core.R;

public class StandardRecommendation extends Recommendation<Standard> {

    public StandardRecommendation(Standard object) {
        super(object, Text.from(R.string.format_recommendation_standard, object.getSuffix(), object.getTitle()));
    }

}
