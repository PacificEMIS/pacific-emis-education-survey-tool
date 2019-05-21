package fm.doe.national.ui.screens.survey.navigation;

import com.omega_r.libs.omegatypes.Text;

import fm.doe.national.R;
import fm.doe.national.ui.screens.base.BaseFragment;

public class ReportNavigationItem implements NavigationItem {

    private static final Text HEADER = Text.from(R.string.title_report_short);
    private static final Text NAME = Text.from(R.string.title_report);

    @Override
    public BaseFragment buildFragment() {
        return null;
    }

    @Override
    public Text getName() {
        return NAME;
    }

    @Override
    public long getHeaderId() {
        return Long.MAX_VALUE;
    }

    @Override
    public Text getHeader() {
        return HEADER;
    }

}
