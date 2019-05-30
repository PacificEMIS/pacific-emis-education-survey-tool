package fm.doe.national.accreditation.ui.navigation;

import com.omega_r.libs.omegatypes.Text;

import fm.doe.national.core.data.model.Progress;

public abstract class ProgressablePrefixedBuildableNavigationItem extends BuildableNavigationItem {

    private Text titlePrefix;
    private long id;

    public ProgressablePrefixedBuildableNavigationItem(long id, Text title, Text titlePrefix) {
        super(title);
        this.titlePrefix = titlePrefix;
        this.id = id;
    }

    public abstract Progress getProgress();

    public abstract void setProgress(Progress progress);

    public Text getTitlePrefix() {
        return titlePrefix;
    }

    public long getId() {
        return id;
    }
}
