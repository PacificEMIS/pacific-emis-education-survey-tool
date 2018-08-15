package fm.doe.national.ui.screens.main;

import android.os.Bundle;

import javax.inject.Inject;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.R;
import fm.doe.national.data.data_source.db.OrmLiteDataSource;
import fm.doe.national.ui.screens.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Inject
    OrmLiteDataSource ormLiteDataSource;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MicronesiaApplication.getAppComponent().inject(this);
        ormLiteDataSource.requestGroupStandard().subscribe(groupStandards -> {
            System.out.println();
        });
    }
}
