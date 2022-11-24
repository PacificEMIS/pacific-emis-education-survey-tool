package org.pacific_emis.surveys.report_core.ui.summary;

import com.omega_r.libs.omegatypes.Text;
import org.pacific_emis.surveys.report_core.R;
import org.pacific_emis.surveys.report_core.ui.base.BaseReportFragment;
import org.pacific_emis.surveys.report_core.ui.level_legend.LevelLegendView;

public abstract class BaseSummaryFragment extends BaseReportFragment implements BaseSummaryView {

    @Override
    public void setLoadingVisibility(boolean visible) {
        // nothing
    }

    @Override
    public Text getTabName() {
        return Text.from(R.string.summary);
    }

    @Override
    public void setHeaderItem(LevelLegendView.Item item) {
        // nothing
    }
}
