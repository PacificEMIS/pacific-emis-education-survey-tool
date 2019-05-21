package fm.doe.national.ui.screens.survey.navigation;

import com.omega_r.libs.omegatypes.Text;

import fm.doe.national.ui.screens.base.BaseFragment;

public interface NavigationItem {

    BaseFragment buildFragment();

    Text getName();

    long getHeaderId();

    Text getHeader();

}
