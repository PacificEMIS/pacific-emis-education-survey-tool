package fm.doe.national.ui.screens.splash;

import com.arellomobile.mvp.InjectViewState;

import fm.doe.national.ui.screens.menu.base.MenuPresenter;

@InjectViewState
public class SplashPresenter extends MenuPresenter<SplashView> {

    public SplashPresenter() {
        getViewState().startAnimate();
    }
}
