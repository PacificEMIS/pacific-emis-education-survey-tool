package fm.doe.national.accreditation.ui.navigation.concrete;

import com.omega_r.libs.omegatypes.Text;

import fm.doe.national.accreditation.R;
import fm.doe.national.survey_core.navigation.NavigationItem;

public class ReportTitleNavigationItem extends NavigationItem {

    public ReportTitleNavigationItem() {
        super(Text.from(R.string.title_report_short), NavigationItem.NO_ID);
    }

}
