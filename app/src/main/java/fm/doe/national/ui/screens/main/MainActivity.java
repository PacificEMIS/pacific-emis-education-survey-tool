package fm.doe.national.ui.screens.main;

import com.arellomobile.mvp.presenter.InjectPresenter;

import fm.doe.national.R;
import fm.doe.national.ui.screens.base.BaseActivity;

public class MainActivity extends BaseActivity {
    @InjectPresenter
    MainPresenter presenter;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }
}
