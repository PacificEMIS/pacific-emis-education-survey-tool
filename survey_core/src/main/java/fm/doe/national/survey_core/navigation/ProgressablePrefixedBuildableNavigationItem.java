package fm.doe.national.survey_core.navigation;

import com.omega_r.libs.omegatypes.Text;

import fm.doe.national.core.data.model.Progress;

public abstract class ProgressablePrefixedBuildableNavigationItem extends PrefixedBuildableNavigationItem {

    public ProgressablePrefixedBuildableNavigationItem(long id, Text title, Text titlePrefix) {
        super(id, title, titlePrefix);
    }

    public abstract Progress getProgress();

    public abstract void setProgress(Progress progress);

}
