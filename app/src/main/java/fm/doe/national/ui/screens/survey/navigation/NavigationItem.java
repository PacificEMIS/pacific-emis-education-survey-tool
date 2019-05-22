package fm.doe.national.ui.screens.survey.navigation;

import androidx.annotation.NonNull;

import com.omega_r.libs.omegatypes.Text;

import fm.doe.national.core.ui.screens.base.BaseFragment;

public interface NavigationItem {

    @NonNull
    BaseFragment buildFragment();

    Text getName();

    long getHeaderId();

    Text getHeader();

}
