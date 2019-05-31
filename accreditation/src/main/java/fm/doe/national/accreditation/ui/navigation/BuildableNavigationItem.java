package fm.doe.national.accreditation.ui.navigation;

import androidx.annotation.NonNull;

import com.omega_r.libs.omegatypes.Text;

import fm.doe.national.core.ui.screens.base.BaseFragment;

public abstract class BuildableNavigationItem extends NavigationItem {

    public BuildableNavigationItem(Text title, long id) {
        super(title, id);
    }

    @NonNull
    public abstract BaseFragment buildFragment();

}
