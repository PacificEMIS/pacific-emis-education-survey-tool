package org.pacific_emis.surveys.wash.navigation;

import com.omega_r.libs.omegatypes.Text;

import org.pacific_emis.surveys.survey_core.navigation.NavigationItem;
import org.pacific_emis.surveys.wash_core.data.model.Group;

public class GroupNavigationItem extends NavigationItem {

    public GroupNavigationItem(Group group) {
        super(Text.from(group.getTitle()), NavigationItem.NO_ID);
    }

}
