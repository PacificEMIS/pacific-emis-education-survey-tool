package fm.doe.national.wash.navigation;

import com.omega_r.libs.omegatypes.Text;

import fm.doe.national.survey_core.navigation.NavigationItem;
import fm.doe.national.wash_core.data.model.Group;

public class GroupNavigationItem extends NavigationItem {

    public GroupNavigationItem(Group group) {
        super(Text.from(group.getTitle()), NavigationItem.NO_ID);
    }

}
