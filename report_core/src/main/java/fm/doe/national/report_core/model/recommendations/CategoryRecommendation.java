package fm.doe.national.report_core.model.recommendations;

import com.omega_r.libs.omegatypes.Text;

import fm.doe.national.accreditation_core.data.model.Category;
import fm.doe.national.report_core.R;

public class CategoryRecommendation extends Recommendation<Category> {
    public CategoryRecommendation(Category object) {
        super(object, Text.from(R.string.format_recommendation_category, object.getTitle()));
    }
}
