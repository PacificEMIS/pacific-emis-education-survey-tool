package org.pacific_emis.surveys.survey_core.navigation;

import com.omega_r.libs.omegatypes.Text;

import org.pacific_emis.surveys.core.data.model.Progress;

public abstract class ProgressablePrefixedBuildableNavigationItem extends PrefixedBuildableNavigationItem {

    public ProgressablePrefixedBuildableNavigationItem(long id, Text title, Text titlePrefix) {
        super(id, title, titlePrefix);
    }

    public abstract Progress getProgress();

    public abstract void setProgress(Progress progress);

}
