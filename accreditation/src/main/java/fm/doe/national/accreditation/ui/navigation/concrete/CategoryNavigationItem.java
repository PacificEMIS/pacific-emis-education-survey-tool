package fm.doe.national.accreditation.ui.navigation.concrete;

import com.omega_r.libs.omegatypes.Text;

import fm.doe.national.accreditation.ui.navigation.NavigationItem;
import fm.doe.national.core.data.model.Category;

public class CategoryNavigationItem extends NavigationItem {

    public CategoryNavigationItem(Category category) {
        super(Text.from(category.getTitle()));
    }

}
