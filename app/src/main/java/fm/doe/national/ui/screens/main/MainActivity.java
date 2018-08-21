package fm.doe.national.ui.screens.main;

import android.os.Bundle;

import com.arellomobile.mvp.presenter.InjectPresenter;

import fm.doe.national.R;
import fm.doe.national.ui.screens.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @InjectPresenter
    MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }


}
