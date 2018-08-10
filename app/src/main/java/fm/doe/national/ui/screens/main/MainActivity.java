package fm.doe.national.ui.screens.main;

import android.os.Bundle;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.R;
import fm.doe.national.ui.screens.base.BaseActivity;

public class MainActivity extends BaseActivity {

    private static final int FILE_REQUEST_CODE = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MicronesiaApplication.getAppComponent().inject(this);

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

}
