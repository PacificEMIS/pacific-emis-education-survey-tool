package fm.doe.national.ui.screens.splash;

import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import butterknife.BindView;
import fm.doe.national.BuildConfig;
import fm.doe.national.R;
import fm.doe.national.ui.screens.base.BaseActivity;
import fm.doe.national.ui.screens.splash_end.SplashEndActivity;


public class SplashActivity extends BaseActivity implements SplashView {

    private static final long DURATION_ANIMATION = 1000;

    @BindView(R.id.progressbar_long_loading)
    ProgressBar longLoadingProgressBar;

    @BindView(R.id.textview_title)
    TextView titleTextView;

    @BindView(R.id.imageview_logo)
    ImageView logoImageView;

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
    public void navigateToSplashEnd() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setExitTransition(null);
        }

        String transitionNameText = ViewCompat.getTransitionName(titleTextView);
        String transitionNameImage = ViewCompat.getTransitionName(logoImageView);

        Intent intent = new Intent(this, SplashEndActivity.class);
        ActivityOptionsCompat optionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                        Pair.create(titleTextView, transitionNameText),
                        Pair.create(logoImageView, transitionNameImage));
        startActivity(intent, optionsCompat.toBundle());
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
}
