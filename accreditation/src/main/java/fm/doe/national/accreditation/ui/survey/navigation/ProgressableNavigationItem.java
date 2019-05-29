package fm.doe.national.accreditation.ui.survey.navigation;

import com.omega_r.libs.omegatypes.Text;

import fm.doe.national.core.data.model.Progressable;

public interface ProgressableNavigationItem extends NavigationItem, Progressable {

    Text getNamePrefix();

}
