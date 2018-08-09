package fm.doe.national.ui.screens.main;

import android.content.Intent;
import android.os.Bundle;

import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;

import butterknife.OnClick;
import fm.doe.national.MicronesiaApplication;
import fm.doe.national.R;
import fm.doe.national.data.converters.DataImporter;
import fm.doe.national.data.data_source.db.DatabaseDataSource;
import fm.doe.national.data.data_source.db.dao.DatabaseHelper;
import fm.doe.national.data.data_source.db.dao.GroupStandardDao;
import fm.doe.national.data.models.survey.GroupStandard;
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
