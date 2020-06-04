package fm.doe.national.ui.screens.logo;

import com.omega_r.libs.omegatypes.image.Image;
import com.omegar.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.omegar.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.omegar.mvp.viewstate.strategy.StateStrategyType;

import fm.doe.national.core.ui.screens.base.BaseView;

public interface LogoView extends BaseView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setLogo(Image logo);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void pickImageFromGallery();

}
