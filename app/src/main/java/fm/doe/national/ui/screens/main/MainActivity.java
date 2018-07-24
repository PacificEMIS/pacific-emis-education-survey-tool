package fm.doe.national.ui.screens.main;

import android.os.Bundle;
import android.util.Log;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.R;
import fm.doe.national.data_source.static_source.StaticDataSource;
import fm.doe.national.ui.screens.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StaticDataSource staticDataSource = MicronesiaApplication.getAppComponent().getStaticDataSource();
        Log.d("TAG", "Size = " + staticDataSource.getSchoolList().size());
    }
}
