package fm.doe.national.ui.screens.splash;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.transition.ChangeBounds;
import android.support.transition.TransitionManager;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.ProgressBar;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.ui.screens.menu.base.MenuActivity;
import fm.doe.national.ui.screens.menu.base.MenuPresenter;

public class SplashActivity extends MenuActivity implements SplashView {

    private static final long DURATION_ANIMATION = 1000;

    @BindView(R.id.layout_splash_start)
    ConstraintLayout constraintLayout;

    @BindView(R.id.progressbar_long_loading)
    ProgressBar longLoadingProgressBar;

    @InjectPresenter
    SplashPresenter splashPresenter;

    @ProvidePresenter
    SplashPresenter providePresenter() {
        return new SplashPresenter(getAssets());
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_splash_start;
    }

    @Override
    public void showSelector() {
        ConstraintSet newConstraintSet = new ConstraintSet();
        newConstraintSet.clone(getApplicationContext(), R.layout.activity_splash_end);
        newConstraintSet.applyTo(constraintLayout);
        TransitionManager.beginDelayedTransition(constraintLayout, new ChangeBounds()
                .setInterpolator(new AccelerateInterpolator())
                .setDuration(DURATION_ANIMATION));
    }

    @Override
    protected MenuPresenter getPresenter() {
        return splashPresenter;
    }

    @Override
    public void showLongLoadingProgressBar() {
        longLoadingProgressBar.setAlpha(0.0f);
        longLoadingProgressBar.setVisibility(View.VISIBLE);
        longLoadingProgressBar.animate()
                .alpha(1.0f)
                .setDuration(DURATION_ANIMATION)
                .setInterpolator(new AccelerateDecelerateInterpolator());
    }

    @Override
    public void hideLongLoadingProgressBar() {
        if (longLoadingProgressBar.getVisibility() == View.GONE) return;

        longLoadingProgressBar.animate()
                .alpha(0.0f)
                .setDuration(DURATION_ANIMATION)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        longLoadingProgressBar.setVisibility(View.GONE);
                    }
                });
    }
}
