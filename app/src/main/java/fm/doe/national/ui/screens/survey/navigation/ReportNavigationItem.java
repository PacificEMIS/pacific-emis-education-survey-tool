package fm.doe.national.ui.screens.survey.navigation;

import com.omega_r.libs.omegatypes.Text;

import org.jetbrains.annotations.NotNull;

import fm.doe.national.R;
import fm.doe.national.ui.screens.base.BaseFragment;

public class ReportNavigationItem implements NavigationItem {

    private static final Text HEADER = Text.from(R.string.title_report_short);
    private static final Text NAME = Text.from(R.string.title_report);

    @NotNull
    @Override
    public BaseFragment buildFragment() {
        throw new UnsupportedOperationException();
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
