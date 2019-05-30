package fm.doe.national.accreditation.ui.navigation;

import com.omega_r.libs.omegatypes.Text;

import fm.doe.national.core.data.model.Progress;

public abstract class ProgressablePrefixedBuildableNavigationItem extends BuildableNavigationItem {

    private Text titlePrefix;

    public ProgressablePrefixedBuildableNavigationItem(Text title, Text titlePrefix) {
        super(title);
    }

    public abstract Progress getProgress();

    public Text getTitlePrefix() {
        return titlePrefix;
    }
}
