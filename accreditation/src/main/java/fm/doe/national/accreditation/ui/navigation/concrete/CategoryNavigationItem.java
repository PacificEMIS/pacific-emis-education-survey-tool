package fm.doe.national.accreditation.ui.navigation.concrete;

import com.omega_r.libs.omegatypes.Text;

import fm.doe.national.accreditation_core.data.model.Category;
import fm.doe.national.survey_core.navigation.NavigationItem;

public class CategoryNavigationItem extends NavigationItem {

    public CategoryNavigationItem(Category category) {
        super(Text.from(category.getTitle()), NavigationItem.NO_ID);
    }

}
