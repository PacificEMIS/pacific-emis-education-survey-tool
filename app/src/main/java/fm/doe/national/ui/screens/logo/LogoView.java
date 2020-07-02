package fm.doe.national.ui.screens.logo;

import com.omega_r.libs.omegatypes.Image;
import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import fm.doe.national.core.ui.screens.base.BaseView;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface LogoView extends BaseView {

    void setLogo(Image logo);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void pickImageFromGallery();

}
