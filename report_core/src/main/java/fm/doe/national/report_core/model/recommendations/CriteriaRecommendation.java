package fm.doe.national.report_core.model.recommendations;

import com.omega_r.libs.omegatypes.Text;

import fm.doe.national.accreditation_core.data.model.Criteria;
import fm.doe.national.report_core.R;

public class CriteriaRecommendation extends Recommendation<Criteria> {

    public CriteriaRecommendation(Criteria object) {
        super(object, Text.from(R.string.format_recommendation_criteria, object.getSuffix(), object.getTitle()));
    }

}
