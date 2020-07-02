package fm.doe.national.survey_core.navigation;

import com.omega_r.libs.omegatypes.Text;

import fm.doe.national.core.data.model.Progress;

public abstract class ProgressablePrefixedBuildableNavigationItem extends BuildableNavigationItem {

    private Text titlePrefix;

    public ProgressablePrefixedBuildableNavigationItem(long id, Text title, Text titlePrefix) {
        super(title, id);
        this.titlePrefix = titlePrefix;
    }

    public abstract Progress getProgress();

    public abstract void setProgress(Progress progress);

    public Text getTitlePrefix() {
        return titlePrefix;
    }

}
