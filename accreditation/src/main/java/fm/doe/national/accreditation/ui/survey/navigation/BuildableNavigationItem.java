package fm.doe.national.accreditation.ui.survey.navigation;

import androidx.annotation.NonNull;

import com.omega_r.libs.omegatypes.Text;

import fm.doe.national.core.ui.screens.base.BaseFragment;

public abstract class BuildableNavigationItem extends NavigationItem {

    public BuildableNavigationItem(Text title) {
        super(title);
    }

    @NonNull
    public abstract BaseFragment buildFragment();

}
