package org.pacific_emis.surveys.accreditation.ui.navigation.concrete;

import com.omega_r.libs.omegatypes.Text;

import org.pacific_emis.surveys.accreditation.R;
import org.pacific_emis.surveys.survey_core.navigation.NavigationItem;

public class ReportTitleNavigationItem extends NavigationItem {

    public ReportTitleNavigationItem() {
        super(Text.from(R.string.title_report_short), NavigationItem.NO_ID);
    }

}
