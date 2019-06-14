package fm.doe.national.ui.screens.splash;

import android.widget.ImageView;

import com.omegar.mvp.presenter.InjectPresenter;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.core.ui.screens.base.BaseActivity;
import fm.doe.national.ui.screens.authentication.AuthenticationActivity;
import fm.doe.national.ui.screens.menu.MainMenuActivity;
import fm.doe.national.ui.screens.password.PasswordsActivity;
import fm.doe.national.ui.screens.region.ChooseRegionActivity;


public class SplashActivity extends BaseActivity implements SplashView {

    @BindView(R.id.imageview_logo)
    ImageView logoImageView;

    @InjectPresenter
    SplashPresenter splashPresenter;

    @Override
    protected int getContentView() {
        return R.layout.activity_splash;
    }

    @Override
    public void navigateToMasterPassword() {
        startActivity(PasswordsActivity.createIntent(this, false));
    }

    @Override
    public void navigateToSignIn() {
        startActivity(AuthenticationActivity.createIntent(this));
    }

    @Override
    public void navigateToRegionChoose() {
        startActivity(ChooseRegionActivity.createIntent(this));
    }

    @Override
    public void navigateToMenu() {
        startActivity(MainMenuActivity.createIntent(this, false));
    }
}
