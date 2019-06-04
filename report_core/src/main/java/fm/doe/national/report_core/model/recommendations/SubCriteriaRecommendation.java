package fm.doe.national.report_core.model.recommendations;

import com.omega_r.libs.omegatypes.Text;

import fm.doe.national.accreditation_core.data.model.SubCriteria;
import fm.doe.national.report_core.R;

public class SubCriteriaRecommendation extends Recommendation<SubCriteria> {

    public SubCriteriaRecommendation(SubCriteria object) {
        super(object, Text.from(R.string.format_recommendation_sub_criteria, object.getSuffix(), object.getTitle()));
    }

}
