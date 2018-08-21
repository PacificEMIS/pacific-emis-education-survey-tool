package fm.doe.national.ui.screens.splash;

import android.os.Handler;
import android.os.Looper;
import android.support.transition.ChangeBounds;
import android.support.transition.Transition;
import android.view.animation.AccelerateInterpolator;

import com.arellomobile.mvp.InjectViewState;

import fm.doe.national.ui.screens.menu.base.MenuPresenter;

/**
 * Created by Alexander Chibirev on 8/10/2018.
 */

@InjectViewState
public class SplashPresenter extends MenuPresenter<SplashView> {

    private static final long DURATION_ANIMATION = 1000; // 1 sec
    private static final long POST_DELAYED = 1000; //ms

    public SplashPresenter() {
        super();
        startPostDelayedAnimate();
    }

    private void startPostDelayedAnimate() {
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> getViewState().startAnimate(createTransition()), POST_DELAYED);
    }

    private Transition createTransition() {
        return new ChangeBounds()
                .setInterpolator(new AccelerateInterpolator())
                .setDuration(DURATION_ANIMATION);
    }

}
