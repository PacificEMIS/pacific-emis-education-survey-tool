package fm.doe.national.accreditation.ui.survey.navigation.concrete;

import androidx.annotation.NonNull;

import com.omega_r.libs.omegatypes.Text;

import fm.doe.national.accreditation.ui.survey.navigation.BuildableNavigationItem;
import fm.doe.national.core.R;
import fm.doe.national.core.ui.screens.base.BaseFragment;
import fm.doe.national.report.ui.report.ReportFragment;

public class ReportNavigationItem extends BuildableNavigationItem {

    public ReportNavigationItem() {
        super(Text.from(R.string.title_report_short));
    }

    @NonNull
    @Override
    public BaseFragment buildFragment() {
        return new ReportFragment();
    }

}
