package fm.doe.national.survey_core.navigation;

import com.omega_r.libs.omegatypes.Text;

public abstract class PrefixedBuildableNavigationItem extends BuildableNavigationItem {

    private Text titlePrefix;

    public PrefixedBuildableNavigationItem(long id, Text title, Text titlePrefix) {
        super(title, id);
        this.titlePrefix = titlePrefix;
    }

    public Text getTitlePrefix() {
        return titlePrefix;
    }

}
