package org.pacific_emis.surveys.accreditation.ui.navigation.concrete;

import com.omega_r.libs.omegatypes.Text;

import org.pacific_emis.surveys.accreditation_core.data.model.Category;
import org.pacific_emis.surveys.survey_core.navigation.NavigationItem;

public class CategoryNavigationItem extends NavigationItem {

    public CategoryNavigationItem(Category category) {
        super(Text.from(category.getTitle()), NavigationItem.NO_ID);
    }

}
