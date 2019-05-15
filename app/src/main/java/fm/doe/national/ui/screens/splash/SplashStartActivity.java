package fm.doe.national.ui.screens.splash;

import android.content.Intent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;

import com.omegar.mvp.presenter.InjectPresenter;
import com.omegar.mvp.presenter.ProvidePresenter;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.ui.screens.base.BaseActivity;
import fm.doe.national.ui.screens.splash_end.SplashEndActivity;


public class SplashStartActivity extends BaseActivity implements SplashStartView {

    private static final long DURATION_ANIMATION = 1000;

    @BindView(R.id.progressbar_long_loading)
    ProgressBar longLoadingProgressBar;

    @BindView(R.id.textview_title)
    TextView titleTextView;

    @BindView(R.id.imageview_logo)
    ImageView logoImageView;

    @InjectPresenter
    SplashStartPresenter splashStartPresenter;

    @ProvidePresenter
    SplashStartPresenter providePresenter() {
        return new SplashStartPresenter(getAssets());
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_splash_start;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void navigateToSplashEnd() {
        String transitionNameText = ViewCompat.getTransitionName(titleTextView);
        String transitionNameImage = ViewCompat.getTransitionName(logoImageView);

        Intent intent = SplashEndActivity.createIntent(this);
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
