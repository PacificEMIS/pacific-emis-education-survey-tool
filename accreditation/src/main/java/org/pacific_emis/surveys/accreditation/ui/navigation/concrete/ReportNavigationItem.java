package org.pacific_emis.surveys.accreditation.ui.navigation.concrete;

import androidx.annotation.NonNull;

import com.omega_r.libs.omegatypes.Text;

import org.pacific_emis.surveys.core.R;
import org.pacific_emis.surveys.core.ui.screens.base.BaseFragment;
import org.pacific_emis.surveys.report.ui.report.ReportFragment;
import org.pacific_emis.surveys.survey_core.navigation.survey_navigator.ReportBuildableNavigationItem;

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
