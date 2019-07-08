package fm.doe.national.ui.screens.authentication;

import android.content.Context;
import android.content.Intent;

import com.omegar.mvp.presenter.InjectPresenter;

import fm.doe.national.R;
import fm.doe.national.core.ui.screens.base.BaseActivity;
import fm.doe.national.ui.screens.region.ChooseRegionActivity;

public class AuthenticationActivity extends BaseActivity implements AuthenticationView {

    @InjectPresenter
    AuthenticationPresenter presenter;

    public static Intent createIntent(Context context) {
        return new Intent(context, AuthenticationActivity.class);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_authentication;
    }

    @Override
    public void navigateToRegionChoose() {
        startActivity(ChooseRegionActivity.createIntent(this));
        finish();
    }
}
