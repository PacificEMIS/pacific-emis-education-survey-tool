package fm.doe.national.survey_core.navigation.survey_navigator;

import com.omega_r.libs.omegatypes.Text;

import fm.doe.national.survey_core.navigation.BuildableNavigationItem;

public abstract class ReportBuildableNavigationItem extends BuildableNavigationItem {

    public ReportBuildableNavigationItem(Text title, long id) {
        super(title, id);
    }

}

