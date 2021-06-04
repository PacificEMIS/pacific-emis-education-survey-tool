package org.pacific_emis.surveys.report_core.model.recommendations;

import com.omega_r.libs.omegatypes.Text;

import org.pacific_emis.surveys.accreditation_core.data.model.Criteria;
import org.pacific_emis.surveys.report_core.R;

public class CriteriaRecommendation extends Recommendation<Criteria> {

    public CriteriaRecommendation(Criteria object) {
        super(object, Text.from(R.string.format_recommendation_criteria, object.getSuffix(), object.getTitle()));
    }

}
