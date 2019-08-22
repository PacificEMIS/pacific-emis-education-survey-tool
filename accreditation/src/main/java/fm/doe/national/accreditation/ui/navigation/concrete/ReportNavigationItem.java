package fm.doe.national.accreditation.ui.navigation.concrete;

import androidx.annotation.NonNull;

import com.omega_r.libs.omegatypes.Text;

import fm.doe.national.core.R;
import fm.doe.national.core.ui.screens.base.BaseFragment;
import fm.doe.national.report.ui.report.ReportFragment;
import fm.doe.national.survey_core.navigation.survey_navigator.ReportBuildableNavigationItem;

public class ReportNavigationItem extends ReportBuildableNavigationItem {

    private static final long ITEM_ID = -2;

    public ReportNavigationItem() {
        super(Text.from(R.string.title_report), ITEM_ID);
    }

    @NonNull
    @Override
    public BaseFragment buildFragment() {
        return new ReportFragment();
    }

}
