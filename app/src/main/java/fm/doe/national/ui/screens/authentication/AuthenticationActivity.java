package fm.doe.national.ui.screens.authentication;

import android.content.Intent;

import com.omegar.mvp.presenter.InjectPresenter;

import fm.doe.national.R;
import fm.doe.national.core.ui.screens.base.BaseActivity;
import fm.doe.national.ui.screens.region.ChooseRegionActivity;

public class AuthenticationActivity extends BaseActivity implements AuthenticationView {

    @InjectPresenter
    AuthenticationPresenter presenter;

    @Override
    protected int getContentView() {
        return R.layout.activity_authentication;
    }

    @Override
    public void navigateToRegionChoose() {
        startActivity(new Intent(this, ChooseRegionActivity.class));
        finish();
    }
}
