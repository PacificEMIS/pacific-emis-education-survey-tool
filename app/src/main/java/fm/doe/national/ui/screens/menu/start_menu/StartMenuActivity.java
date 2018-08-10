package fm.doe.national.ui.screens.menu.start_menu;

import android.os.Bundle;
import android.support.annotation.LayoutRes;

import com.arellomobile.mvp.presenter.InjectPresenter;

import butterknife.ButterKnife;
import fm.doe.national.R;
import fm.doe.national.ui.screens.menu.base.BaseMenuActivity;
import fm.doe.national.ui.screens.menu.base.BaseMenuPresenter;
import fm.doe.national.ui.screens.menu.drawer.BaseDrawerActivity;
import fm.doe.national.ui.screens.shool_accreditation.SchoolAccreditationActivity;

/**
 * Created by Alexander Chibirev on 8/9/2018.
 */

public class StartMenuActivity extends BaseMenuActivity implements StartMenuView {

    @InjectPresenter
    StartMenuPresenter startMenuPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        ButterKnife.bind(this);
    }

    @Override
    protected BaseMenuPresenter getPresenter() {
        return startMenuPresenter;
    }

    @Override
    public void showSchoolAccreditationScreen() {
        startActivity(SchoolAccreditationActivity.createIntent(this));
    }

}
