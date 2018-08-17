package fm.doe.national.ui.screens.main;

import android.os.Bundle;

import javax.inject.Inject;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.R;
import fm.doe.national.data.data_source.db.OrmLiteDataSource;
import fm.doe.national.data.data_source.models.db.OrmLiteSchool;
import fm.doe.national.ui.screens.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

}
